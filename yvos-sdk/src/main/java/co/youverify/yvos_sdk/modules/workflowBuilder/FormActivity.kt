package co.youverify.yvos_sdk.modules.workflowBuilder

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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import co.youverify.yvos_sdk.JsObject
import co.youverify.yvos_sdk.R
import co.youverify.yvos_sdk.components.ModalWindow
import co.youverify.yvos_sdk.components.ProgressIndicator
import co.youverify.yvos_sdk.exceptions.SdkException
import co.youverify.yvos_sdk.theme.SdkTheme
import co.youverify.yvos_sdk.util.FINISH_ACTIVITY
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

internal class FormActivity : AppCompatActivity() {

    private var cameraPermissionChecked: Boolean=false
    var urlIsLoading: Boolean = false
    private set
    private var pageIsAlreadyLoading: Boolean=false
    private var url: String?=null
    private var userName: String?=null
    var cameraPermissionGranted: Boolean=false
    private set
    lateinit var modalWindowView: ComposeView
    private val modalWindowVisible=mutableStateOf(false)
    private val progressIndicatorVisible=mutableStateOf(true)
    private var pageReady: Boolean=false
    private lateinit var webView: WebView
    private val TAG="FormActivity"
    //private lateinit var closeButton: ImageView
    private lateinit var progressIndicatorView: ComposeView
    private var fileChooserValueCallback: ValueCallback<Array<Uri>>? = null
    private var fileChooserLauncher: ActivityResultLauncher<Intent> = createFileChooserLauncher()
    private val cameraPermissionRequestLauncher:ActivityResultLauncher<String> = createCameraPermissionRequestLauncher()
    lateinit var onFailed:()->Unit
    lateinit var onSuccess:(String)->Unit
    lateinit var onCompleted:(String)->Unit



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(WorkflowBuilderModule.formActivityObserver)

        setContentView(R.layout.activity_form)
        initializeViews()
        setUpWebView()



        /*onBackPressedDispatcher.addCallback(this,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                return
            //if (pageReady) return else finish()
            }
        })*/



    }

    override fun onStart() {
        super.onStart()
        if (!cameraPermissionChecked){ checkForCameraPermission() }
    }

    private fun initializeViews() {

        //closeButton=findViewById(R.id.form_close_button)
        val customization= WorkflowBuilderModule.formActivityObserver.workflowBuilderModule.customization
        progressIndicatorView=findViewById(R.id.progressIndicatorView)
        modalWindowView=findViewById(R.id.modalWindowView)
        webView=findViewById(R.id.webView_Vform)

        userName= intent.getStringExtra(USER_NAME)

        progressIndicatorView.setContent {
            ProgressIndicator(colorString =customization.primaryColor , visible =progressIndicatorVisible.value )
        }

        modalWindowView.setContent {


            SdkTheme {
                ModalWindow(
                    name =userName?:"" ,
                    onActionButtonClicked = {

                        modalWindowVisible.value=false
                        webView.visibility=View.VISIBLE
                    },
                    visible = modalWindowVisible.value,
                    buttonBackGroundColorString = customization.buttonBackgroundColor,
                    buttonTextColorString = customization.buttonTextColor,
                    buttonText = customization.actionText,
                    greeting = customization.greeting
                )
            }

        }

       /* closeButton.setOnClickListener{
            finish()
        }*/
    }

    private fun checkForCameraPermission() {

        cameraPermissionChecked=true
        if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            cameraPermissionGranted=true

            //load url if url is not presently null otherwise call VFormModule class to send it
            if (url!=null){
                webView.loadUrl(url!!)
            }else{
                urlIsLoading=true
                WorkflowBuilderModule.formActivityObserver.sendFormUrl()
            }

        }

        else{
            //hide the progress indicator composable
            progressIndicatorVisible.value=false
            cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA)

        }
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

                override fun onPageStarted(view: WebView?, url: kotlin.String?, favicon: Bitmap?) {
                    pageIsAlreadyLoading=true
                }

                override fun onPageFinished(view: WebView?, url: kotlin.String?) {

                    if (view?.progress==100){

                        view.loadUrl(
                            "javascript:(function() {" +
                                    "window.parent.addEventListener ('message', function(event) {" +
                                    " Android.receiveMessage(JSON.stringify(event.data));});" +
                                    "})()"
                        )

                        progressIndicatorVisible.value=false

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

                override fun getDefaultVideoPoster(): Bitmap? {
                    //replace the default play button with a transparent background
                    return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
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


    private fun createCameraPermissionRequestLauncher(): ActivityResultLauncher<kotlin.String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it){
                cameraPermissionGranted=true

                //show the progress indicator composable
                progressIndicatorVisible.value=true

                if (url!=null){
                    webView.loadUrl(url!!)
                }else{
                    WorkflowBuilderModule.formActivityObserver.sendFormUrl()
                }
            }
            else{

                //hide the progress indicator composable
                progressIndicatorVisible.value=false

                Snackbar.make(
                    webView.rootView,
                    "You need to grant Camera Permission to proceed with the process",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Grant permission") { cameraPermissionRequestLauncher.launch(Manifest.permission.CAMERA) }
                    .show()
            }

        }


    private fun createFileChooserLauncher(): ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fileChooserValueCallback?.onReceiveValue(arrayOf(Uri.parse(it?.data?.dataString)));
            } else {
                fileChooserValueCallback?.onReceiveValue(null)
            }
        }


    fun onFormDataReceived(data: kotlin.String) {




        //When form submission fails
        //If the browser event received does not contain Form Data
        if (data.contains("origin")){
            return
        }

        if(data.contains(FormResultType.COMPLETED.id)){

            Log.d("FormActivity",data)
            val formData= Gson().fromJson(data,FormResultData::class.java)
            onCompleted(data)
            return

        }

        if(data.contains(FormResultType.SUCCESS.id)){
            Log.d("FormActivity",data)
            val formData= Gson().fromJson(data,FormResultData::class.java)
            onSuccess(data)
            return
        }

        if(data.contains(FormResultType.FAILURE.id)){
            Log.d("FormActivity",data)
            val formData= Gson().fromJson(data,FormResultData::class.java)
            onFailed()
            return
        }



    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        url=intent?.getStringExtra(URL_TO_DISPLAY)
        url?.let{

            if (cameraPermissionGranted) webView.loadUrl(url!!)
        }

        val finishActivity = intent?.getBooleanArrayExtra(FINISH_ACTIVITY)
        finishActivity?.let {
            if (it.first()) finish()
        }
    }
}