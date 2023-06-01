package co.youverify.yvos_sdk.modules.vform

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import co.youverify.yvos_sdk.R
import co.youverify.yvos_sdk.components.ModalWindow
import co.youverify.yvos_sdk.theme.SdkTheme
import co.youverify.yvos_sdk.util.SdkException
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class FormActivity : AppCompatActivity() {

    private var pageIsAlreadyLoading: Boolean=false
    private var url: String?=null
    private var userName: String?=null
    private var cameraPermissionGranted: Boolean=false
    lateinit var composeView: ComposeView
     private val modalWindowVisible=mutableStateOf(false)
    private var pageReady: Boolean=false
    private lateinit var webView: WebView
    private val TAG="FormActivity"
    //private lateinit var closeButton: ImageView
    private lateinit var progressBar: ProgressBar
    private var fileChooserValueCallback: ValueCallback<Array<Uri>>? = null
    private var fileChooserLauncher: ActivityResultLauncher<Intent> = createFileChooserLauncher()
    private val cameraPermissionRequestLauncher:ActivityResultLauncher<String> = createCameraPermissionRequestLauncher()
    lateinit var onFailed:(VFormEntryData?)->Unit
    lateinit var onSuccess:(VFormEntryData?)->Unit
    lateinit var onCompleted:(VFormEntryData?)->Unit



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        initializeViews()
        setUpWebView()
        checkForCameraPermission()


        /*onBackPressedDispatcher.addCallback(this,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                return
            //if (pageReady) return else finish()
            }
        })*/

        lifecycle.addObserver(VFormModule.formActivityObserver)

    }

    private fun initializeViews() {

        //closeButton=findViewById(R.id.form_close_button)
        progressBar=findViewById(R.id.progressBar2)
        composeView=findViewById(R.id.composeView)
        webView=findViewById(R.id.webView_Vform)


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

       /* closeButton.setOnClickListener{
            finish()
        }*/
    }

    private fun checkForCameraPermission() {
        if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            cameraPermissionGranted=true

            //load url if url is not presently null i.e if it has been sent by VformModule class
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
            settings.loadWithOverviewMode=true
            settings.useWideViewPort=true
            settings.builtInZoomControls=true
            settings.displayZoomControls=false
            addJavascriptInterface(JsObject(this@FormActivity),"Android")
            setInitialScale(1)

            //Set up the Webview client
            webViewClient= object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    pageIsAlreadyLoading=true
                }

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

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    val b=newProgress
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
            if (it){
                cameraPermissionGranted=true
                url?.let {
                    webView.loadUrl(url!!)
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


    fun onFormDataReceived(data: String) {




        //When form submission fails
        //If the browser event received does not contain Form Data
        if (data.contains("origin")){
            return
        }

        if(data.contains(FormResultType.COMPLETED.id)){

            Log.d("FormActivity",data)
            val formData= Gson().fromJson(data,VFormResultData::class.java)
            onCompleted(formData.data?.entry)
            return

        }

        if(data.contains(FormResultType.SUCCESS.id)){
            Log.d("FormActivity",data)
            val formData= Gson().fromJson(data,VFormResultData::class.java)
            onSuccess(formData.data?.entry)
            return
        }

        if(data.contains(FormResultType.FAILURE.id)){
            Log.d("FormActivity",data)
            val formData= Gson().fromJson(data,VFormResultData::class.java)
            onFailed(formData.data?.entry)
            return
        }



    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        url=intent?.getStringExtra(URL_TO_DISPLAY)
        url?.let{

            if (cameraPermissionGranted) webView.loadUrl(url!!)
        }
    }
}