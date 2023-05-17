package co.youverify.workflow_builder_sdk.modules.documentcapture

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import co.youverify.workflow_builder_sdk.R
import co.youverify.workflow_builder_sdk.data.LivenessRequest
import co.youverify.workflow_builder_sdk.data.LivenessResponse
import co.youverify.workflow_builder_sdk.data.SdkServiceFactory
import co.youverify.workflow_builder_sdk.databinding.ActivityDocumentCaptureBinding
import co.youverify.workflow_builder_sdk.modules.livenesscheck.LivenessOption
import co.youverify.workflow_builder_sdk.modules.vform.JsObject
import co.youverify.workflow_builder_sdk.util.NetworkResult
import co.youverify.workflow_builder_sdk.util.SdkException
import co.youverify.workflow_builder_sdk.util.URL_TO_DISPLAY
import co.youverify.workflow_builder_sdk.util.handleApi
import java.io.IOException

class DocumentCaptureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentCaptureBinding

    private var pageReady: Boolean=false
    private  var url: String?=null
    private lateinit var webView: WebView
    private val TAG="FormActivity"
    private lateinit var closeButton: ImageView
    private lateinit var progressBar: ProgressBar
    private val cameraPermissionRequestLauncher: ActivityResultLauncher<String> = createCameraPermissionRequestLauncher()
    lateinit var onClose:(DocumentResultData)->Unit
    lateinit var onSuccess:()->Unit
    lateinit var onCancel:(DocumentResultData)->Unit
    lateinit var webMessageListener: WebViewCompat.WebMessageListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentCaptureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        url=intent.extras?.getString(URL_TO_DISPLAY)

        initializeViews()


        //Add a Listener for window postMessage event
        webMessageListener=
            WebViewCompat.WebMessageListener { view, message, sourceOrigin, isMainFrame, replyProxy ->

                //val a=Gson().fromJson(message.data,LivenessResultData::class.java)
                when (message.data) {
                    "yvos:document:success" -> {
                        progressBar.visibility = View.VISIBLE
                    }

                    "yvos:document:closed" -> {}

                    "yvos:document:cancelled" -> {}
                }
            }

        //Disable back button
        onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (pageReady) return else finish()
            }
        })

        lifecycle.addObserver(DocumentCaptureModule.documentActivityObserver)


        setUpWebView()
        loadUrl()


    }



    private suspend fun postLivenessData(option: LivenessOption) {

        val request = LivenessRequest(
            publicMerchantID = option.publicMerchantKey,
            faceImage = "",
            passed = true,
            metadata = option.metadata
        )

        val response: NetworkResult<LivenessResponse>
        try {
            response= handleApi { SdkServiceFactory.sdkService(option).postLivenessData( livenessRequest = request) }
            handleResponse(response)
        }catch (e: IOException){
            progressBar.visibility= View.VISIBLE
            Snackbar.make(
                webView.rootView,
                "Could not connect to server, check your internet connection",
                Snackbar.LENGTH_INDEFINITE
            )
                .show()
        }


    }

    private fun initializeViews() {

        webView=findViewById(R.id.webView_document_capture)
        closeButton=findViewById(R.id.document_close_button)
        progressBar=findViewById(R.id.progressBar)
        closeButton.setOnClickListener{
            //onClose(LivenessResultData(passed = true, photo = ""))
            finish()
        }
    }

    private fun loadUrl() {
        if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
            if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
                WebViewCompat.addWebMessageListener(webView, "myObject", setOf("https://os.youverify.co"), webMessageListener)
                webView.loadUrl(url!!)
            }else{throw Exception("Webview need to be updated on client's app")}
        }

        else
            cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {

        url=intent?.extras?.getString(URL_TO_DISPLAY)

        webView.apply {

            settings.javaScriptEnabled=true
            settings.domStorageEnabled=true
            settings.allowContentAccess=true
            settings.allowFileAccess=true
            settings.loadWithOverviewMode=true
            settings.useWideViewPort=true
            settings.builtInZoomControls=true
            settings.displayZoomControls=false
            addJavascriptInterface(JsObject(this@DocumentCaptureActivity),"Android")
            setInitialScale(1)

            //Set up the Webview client
            webViewClient= object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    progressBar.visibility= View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    if(progressBar.isVisible) progressBar.visibility= View.INVISIBLE
                    pageReady=true

                    view?.loadUrl(
                        "javascript:(function() {" +
                                "window.parent.addEventListener ('message', function(event) {" +
                                " Android.receiveMessage(JSON.stringify(event.data));});" +
                                "})()"
                    )
                }


            }



            //Set up WebChrome client
            webChromeClient=object : WebChromeClient(){

                /*override fun onProgressChanged(view: WebView?, newProgress: Int) {

                    if (view?.url?.contains("confirmation")==true){
                        lifecycleScope.launch { delay(2000) }
                        onSuccess()
                        finish()
                    }
                }*/

                override fun onPermissionRequest(request: PermissionRequest?) {
                    Log.d(TAG, "OnPermissionRequest")
                    request?.grant(request.resources);
                }
            }

        }
    }


    private fun createCameraPermissionRequestLauncher(): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
                    WebViewCompat.addWebMessageListener(webView, "myObject", setOf("https://os.youverify.co"), webMessageListener)
                    webView.loadUrl(url!!)
                }else{throw SdkException("Webview need to be updated on client's app")
                }
            }
            else
                Snackbar.make(
                    webView.rootView,
                    "You need to grant Camera Permission to proceed with the process",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Grant permission") { cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA) }
                    .show()
        }


    private fun handleResponse(response: NetworkResult<LivenessResponse>) {

        if(response is NetworkResult.Success)   {
            progressBar.visibility= View.INVISIBLE
            onSuccess()
            finish()
        }
        if(response is NetworkResult.Error){
            if (response.code==404)
                throw SdkException("Invalid credentials- Either the  Public Merchant key is incorrect" +
                        " or the wrong 'dev' argument was supplied")
            else
                throw SdkException(response.message?:"An unexpected error occurred")
        }


    }


    fun onDocumentDataReceived(data: String) {

        Log.d("DocumentCaptureActivity",data)
        val a=data
    }


}