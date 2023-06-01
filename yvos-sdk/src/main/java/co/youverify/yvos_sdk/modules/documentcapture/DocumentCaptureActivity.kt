package co.youverify.yvos_sdk.modules.documentcapture

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import co.youverify.yvos_sdk.R
import co.youverify.yvos_sdk.components.ModalWindow
import co.youverify.yvos_sdk.data.DocumentRequest
import co.youverify.yvos_sdk.data.DocumentResponse
import co.youverify.yvos_sdk.data.SdkServiceFactory
import co.youverify.yvos_sdk.modules.vform.JsObject
import co.youverify.yvos_sdk.theme.SdkTheme
import co.youverify.yvos_sdk.util.NetworkResult
import co.youverify.yvos_sdk.util.SdkException
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.handleApi
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.IOException

class DocumentCaptureActivity : AppCompatActivity() {

    private val url: String?=null
    private var cameraPermissionGranted=false
    private val modalWindowVisible = mutableStateOf(false)
    private lateinit var composeView: ComposeView
    private var userName: String?=null
    private var pageReady: Boolean=false
    private lateinit var webView: WebView
    private val TAG="FormActivity"
    //private lateinit var closeButton: ImageView
    private lateinit var progressBar: ProgressBar
    private val cameraPermissionRequestLauncher: ActivityResultLauncher<String> = createCameraPermissionRequestLauncher()
    lateinit var onClose:(DocumentData?)->Unit
    lateinit var onSuccess:(DocumentData?)->Unit
    lateinit var onCancel:(DocumentData?)->Unit



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_capture)


        initializeViews()
        setUpWebView()
        checkForCameraPermission()

        //Disable back button
       /* onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                //if (pageReady) return else finish()
                return
            }
        })*/

        lifecycle.addObserver(DocumentCaptureModule.documentActivityObserver)




    }




    private fun initializeViews() {

        webView=findViewById(R.id.webView_document_capture)
        composeView=findViewById(R.id.composeView)
        //closeButton=findViewById(R.id.document_close_button)
        progressBar=findViewById(R.id.progressBar)

        /*closeButton.setOnClickListener{
            finish()
        }*/

        userName= intent.getStringExtra(USER_NAME)

        composeView.setContent {

            SdkTheme {
                ModalWindow(
                    name =userName?:"" ,
                    onStartButtonClicked = {
                        modalWindowVisible.value=false
                        webView.visibility=View.VISIBLE
                    },
                    visible = modalWindowVisible.value
                )
            }

        }
    }

    private fun checkForCameraPermission() {
        if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){

            cameraPermissionGranted=true
            //load url if url is not presently null i.e if it has been sent by DocumentCaptureModule class
            url?.let{
                webView.loadUrl(it)
            }
        }


        else
            cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {

        webView.apply {
            settings.javaScriptEnabled=true
            settings.domStorageEnabled=true
            settings.allowContentAccess=true
            settings.allowFileAccess=true
            //settings.loadWithOverviewMode=true
            //settings.useWideViewPort=true
            settings.builtInZoomControls=true
            settings.displayZoomControls=false
            addJavascriptInterface(JsObject(this@DocumentCaptureActivity),"Android")
            //setInitialScale(300)
            setInitialScale(180)

            //Set up the Webview client
            webViewClient= object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {

                    if (view?.progress==100){

                        view.loadUrl(
                            "javascript:(function() {" +
                                    "window.parent.addEventListener ('message', function(event) {" +
                                    " Android.receiveMessage(JSON.stringify(event.data));});" +
                                    "})()"
                        )

                        progressBar.visibility=View.INVISIBLE

                        if (userName.isNullOrEmpty())
                            webView.visibility=View.VISIBLE
                        else
                            modalWindowVisible.value=true
                    }
                }

            }

            //Set up WebChrome client
            webChromeClient=object : WebChromeClient(){

                override fun onPermissionRequest(request: PermissionRequest?) {
                    Log.d(TAG, "OnPermissionRequest")
                    request?.grant(request.resources);
                }
            }

        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val url=intent?.getStringExtra(URL_TO_DISPLAY)
        if (cameraPermissionGranted) {
            url?.let {
                webView.loadUrl(it)
            }
        }
    }

    fun onDocumentDataReceived(data: String) {

        //If the browser event sent does not contain document data
        if (data.contains("origin")){
            return
        }

        val documentResultData:DocumentResultData= Gson().fromJson(data,DocumentResultData::class.java)
        if(data.contains(DocumentResultType.SUCCESS.id)){
                Log.d("DocumentCaptureActivity",data)
                onSuccess(documentResultData.data)
                //postScannedData(documentResultData.data)
            return

        }

        if(data.contains(DocumentResultType.CLOSED.id)){
            Log.d("DocumentCaptureActivity",data)
            onClose(documentResultData.data)
            return
        }

        if(data.contains(DocumentResultType.CANCELLED.id)){
            Log.d("DocumentCaptureActivity",data)
            onClose(documentResultData.data)
            return

        }

    }

    private fun postScannedData(data: DocumentData?) {

        progressBar.visibility= View.INVISIBLE
        val option=DocumentCaptureModule.documentActivityObserver.option
        val request = DocumentRequest(
            publicMerchantID = option.publicMerchantKey,
            documentNumber = data?.documentNumber?:"",
            documentType = "id"
        )

        var response: NetworkResult<DocumentResponse>

        lifecycleScope.launch {
            try {
                response= handleApi { SdkServiceFactory.sdkService(option).postDocumentData(request) }
                handleResponse(response,data)
            }catch (e: IOException){
                progressBar.visibility= View.INVISIBLE
                Snackbar.make(
                    webView.rootView,
                    "Could not connect to server, check your internet connection",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .show()
            }
        }

    }

    private fun handleResponse(response: NetworkResult<DocumentResponse>, data: DocumentData?) {

        if(response is NetworkResult.Success)   {
            progressBar.visibility= View.INVISIBLE
            //onSuccess(data)
            //finish()
        }
        if(response is NetworkResult.Error){
            if (response.code==404)
                throw SdkException("Invalid credentials- Either the  Public Merchant key is incorrect" +
                        " or the wrong 'dev' argument was supplied")
            else
                throw SdkException(response.message?:"An unexpected error occurred")
        }


    }

    private fun createCameraPermissionRequestLauncher(): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {granted->

            if (granted){

                cameraPermissionGranted=true
                url?.let {
                    webView.loadUrl(it)
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


}