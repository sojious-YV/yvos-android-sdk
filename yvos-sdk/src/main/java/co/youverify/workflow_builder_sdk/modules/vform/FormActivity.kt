package co.youverify.workflow_builder_sdk.modules.vform

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.webkit.JavaScriptReplyProxy
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewCompat.WebMessageListener
import androidx.webkit.WebViewFeature
import co.youverify.workflow_builder_sdk.R
import co.youverify.workflow_builder_sdk.util.SdkException
import co.youverify.workflow_builder_sdk.util.URL_TO_DISPLAY
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FormActivity : AppCompatActivity(),WebMessageListener {


    private lateinit var webMessageListener: WebViewCompat.WebMessageListener
    private var pageReady: Boolean=false
    private  var url: String?=null
    private lateinit var webView: WebView
    private val TAG="FormActivity"
    private lateinit var closeButton: ImageView
    private lateinit var progressBar: ProgressBar
    private var fileChooserValueCallback: ValueCallback<Array<Uri>>? = null
    private var fileChooserLauncher: ActivityResultLauncher<Intent> = createFileChooserLauncher()
    private val cameraPermissionRequestLauncher:ActivityResultLauncher<String> = createCameraPermissionRequestLauncher()
    lateinit var onClose:()->Unit
    lateinit var onSuccess:(VFormResultData)->Unit






    val lambdaClass: Class<out (String) -> Unit> = { _: String -> }.javaClass
    private val lambdaClass2= {}.javaClass




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        initializeViews()
        setUpWebView()
        loadUrl()
        //webMessageListener=
            //WebViewCompat//.WebMessageListener { view, message, sourceOrigin, isMainFrame, replyProxy ->

                //val a=message
                //val a=Gson().fromJson(message.data,LivenessResultData::class.java)
           // }





        onBackPressedDispatcher.addCallback(this,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (pageReady) return else finish()
            }
        })

        lifecycle.addObserver(VFormModule.formActivityObserver)

    }

    private fun initializeViews() {
        closeButton=findViewById(R.id.form_close_button)
        progressBar=findViewById(R.id.progressBar2)
        closeButton.setOnClickListener{
            onClose()
            finish()
        }
    }

    private fun loadUrl() {
        if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
                WebViewCompat.addWebMessageListener(webView, "window", setOf("*"), this)
                webView.loadUrl(url!!)
            }else{
                throw SdkException("Webview need to be updated on client app")
            }
        }
            //webView.loadUrl(url!!)
        else
            cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {

         url=intent?.extras?.getString(URL_TO_DISPLAY)

       /* val onSuccessCallback:()->Unit

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            onSuccessCallback=intent?.extras?.getSerializable(FORM_SUBMISSION_SUCCESS_LAMBDA,lambdaClass2.javaClass) as ()->Unit
            onCloseCallback=intent?.extras?.getSerializable(FORM_ON_CLOSE_LAMBDA,lambdaClass2.javaClass) as ()->Unit
        }

        else{
            onSuccessCallback=intent?.extras?.getSerializable(FORM_SUBMISSION_SUCCESS_LAMBDA) as ()->Unit
            onCloseCallback=intent?.extras?.getSerializable(FORM_ON_CLOSE_LAMBDA) as ()->Unit
        }*/


        webView=findViewById(R.id.webView_Vform)
        webView.apply {

            settings.javaScriptEnabled=true
            settings.domStorageEnabled=true
            settings.allowContentAccess=true
            settings.allowFileAccess=true
            settings.loadWithOverviewMode=true
            settings.useWideViewPort=true
            settings.builtInZoomControls=true
            settings.displayZoomControls=false
            addJavascriptInterface(JsObject(this@FormActivity),"Android")
            setInitialScale(1)

            //Set up the Webview client
            webViewClient= object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) { progressBar.visibility=View.VISIBLE }
                override fun onPageFinished(view: WebView?, url: String?) {
                    if(progressBar.isVisible) progressBar.visibility=View.INVISIBLE
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

                override fun onProgressChanged(view: WebView?, newProgress: Int) {

                    if (view?.url?.contains("confirmation")==true){
                        lifecycleScope.launch { delay(2000) }
                        onSuccess(VFormResultData(data = Data(entry = Entry(id="", fields = emptyList()))))
                        finish()
                    }
                }

                override fun onPermissionRequest(request: PermissionRequest?) {
                    Log.d(TAG, "OnPermissionRequest")
                    request?.grant(request.resources);
                }

                override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                    try {
                        fileChooserValueCallback = filePathCallback;
                        fileChooserLauncher.launch(fileChooserParams?.createIntent())
                    } catch (e: ActivityNotFoundException) {
                        // You may handle "No activity found to handle intent" error
                        throw SdkException("No Activity Found to handle File chooser Intent")
                    }
                    return true
                }
            }

            // loadUrl("https://os.dev.youverify.co/v-forms/62b3145af070d00d3c0c41e6?accessId=642ed606c451434de8dabb68")
            //loadUrl("https://os.dev.youverify.co/v-forms/6418559951282f74e34472e9")
            //https://os.dev.youverify.co/v-forms/6418559951282f74e34472e9/confirmation/644bdb86c45143bff8dac6e6?accessId=644bdb86c45143bff8dac6e6&businessId=61d880f1e8e15aaf24558f1a

        }
    }


    private fun createCameraPermissionRequestLauncher(): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                if (WebViewFeature.isFeatureSupported(WebViewFeature.WEB_MESSAGE_LISTENER)) {
                    WebViewCompat.addWebMessageListener(webView, "window", setOf("*"), this)
                    webView.loadUrl(url!!)
                }else{
                    throw SdkException("Webview need to be updated on client app")
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


    private fun createFileChooserLauncher(): ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fileChooserValueCallback?.onReceiveValue(arrayOf(Uri.parse(it?.data?.dataString)));
            } else {
                fileChooserValueCallback?.onReceiveValue(null)
            }
        }

    override fun onPostMessage(
        view: WebView,
        message: WebMessageCompat,
        sourceOrigin: Uri,
        isMainFrame: Boolean,
        replyProxy: JavaScriptReplyProxy
    ) {
        val newVal = message
    }

    fun onFormDataReceived(data: String) {
        val a=data+"you"
    }


}