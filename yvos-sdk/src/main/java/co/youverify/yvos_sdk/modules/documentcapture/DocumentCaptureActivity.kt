package co.youverify.yvos_sdk.modules.documentcapture

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import co.youverify.yvos_sdk.R
import co.youverify.yvos_sdk.components.ModalWindow
import co.youverify.yvos_sdk.components.ProgressIndicator
import co.youverify.yvos_sdk.data.DocumentRequest
import co.youverify.yvos_sdk.data.DocumentResponse
import co.youverify.yvos_sdk.data.SdkServiceFactory
import co.youverify.yvos_sdk.JsObject
import co.youverify.yvos_sdk.components.LoadingDialog
import co.youverify.yvos_sdk.exceptions.InvalidCredentialsException
import co.youverify.yvos_sdk.exceptions.SdkException
import co.youverify.yvos_sdk.theme.SdkTheme
import co.youverify.yvos_sdk.util.FINISH_ACTIVITY
import co.youverify.yvos_sdk.util.NetworkResult
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.handleApi
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.IOException

internal class DocumentCaptureActivity : AppCompatActivity() {

    private val dialogVisible= mutableStateOf(false)
    private var cameraPermissionChecked=false
    private val progressIndicatorVisible= mutableStateOf(true)
    private val url: String?=null
    var cameraPermissionGranted=false
    private set
    private val modalWindowVisible = mutableStateOf(false)
    private lateinit var modalWindowView: ComposeView
    private lateinit var progressIndicatorView: ComposeView
    private var userName: String?=null
    private var pageReady: Boolean=false
    private lateinit var webView: WebView
    private lateinit var loadingDialogView: ComposeView
    private val TAG="FormActivity"
    //private lateinit var closeButton: ImageView
    private val cameraPermissionRequestLauncher: ActivityResultLauncher<String> = createCameraPermissionRequestLauncher()
    lateinit var onClose:()->Unit
    lateinit var onSuccess:(DocumentData)->Unit
    lateinit var onCancel:()->Unit



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_capture)


        lifecycle.addObserver(DocumentCaptureModule.documentActivityObserver)

        initializeViews()
        setUpWebView()


        //Disable back button
       /* onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                //if (pageReady) return else finish()
                return
            }
        })*/
    }


    override fun onStart(){
        super.onStart()
        if (!cameraPermissionChecked){ checkForCameraPermission() }
    }
    private fun initializeViews() {

        webView=findViewById(R.id.webView_document_capture)
        modalWindowView=findViewById(R.id.modalWindowView)
        //closeButton=findViewById(R.id.document_close_button)
        progressIndicatorView=findViewById(R.id.progressIndicatorView)
        loadingDialogView=findViewById(R.id.loadingDialogView)

        /*closeButton.setOnClickListener{
            finish()
        }*/

        userName= intent.getStringExtra(USER_NAME)

        val customization= DocumentCaptureModule.documentActivityObserver.documentCaptureModule.customization


        progressIndicatorView.setContent {
            ProgressIndicator(colorString =customization.primaryColor , visible =progressIndicatorVisible.value )
        }
        loadingDialogView.setContent {
            LoadingDialog(visible = dialogVisible.value)
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
    }

    private fun checkForCameraPermission() {
        cameraPermissionChecked=true
        if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){

            cameraPermissionGranted=true


            if (url!=null){
                webView.loadUrl(url)
            }else{
                DocumentCaptureModule.documentActivityObserver.sendDocumentCaptureUrl()
            }
        }


        else{

            //Hide the progress indicator composable
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

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {

        webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowContentAccess = true
            settings.allowFileAccess = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.builtInZoomControls = true
            settings.displayZoomControls = false
            addJavascriptInterface(JsObject(this@DocumentCaptureActivity),"Android")
            //setInitialScale(300)
            setInitialScale(1)

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

                override fun onPermissionRequest(request: PermissionRequest?) {
                    Log.d(TAG, "OnPermissionRequest")
                    request?.grant(request.resources);
                }

                override fun getDefaultVideoPoster(): Bitmap? {

                    //replace the default play button with a transparent background
                    return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
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

        val finishActivity = intent?.getBooleanArrayExtra(FINISH_ACTIVITY)
        finishActivity?.let {
            if (it.first()) finish()
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
                postScannedData(documentResultData.data!!)
            return

        }

        if(data.contains(DocumentResultType.CLOSED.id)){
            Log.d("DocumentCaptureActivity",data)
            onClose()
            return
        }

        if(data.contains(DocumentResultType.CANCELLED.id)){
            Log.d("DocumentCaptureActivity",data)
            onClose()
            return

        }

    }

    private fun postScannedData(data: DocumentData) {

        dialogVisible.value=true
        val module=DocumentCaptureModule.documentActivityObserver.documentCaptureModule
        val request = DocumentRequest(
            publicMerchantID = module.publicMerchantKey,
            documentNumber = data.documentNumber,
            documentType = "id_card"
        )

        var response: NetworkResult<DocumentResponse>

        lifecycleScope.launch {
            try {
                response= handleApi { SdkServiceFactory.sdkService(module).postDocumentData(request) }
                handleResponse(response,data)
            }catch (e: IOException){
                //progressBar.visibility= View.INVISIBLE
                dialogVisible.value=false
                Snackbar.make(
                    webView.rootView,
                    "Could not connect to server, check your internet connection",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .show()
            }
        }

    }

    private fun handleResponse(response: NetworkResult<DocumentResponse>, data: DocumentData) {

        if(response is NetworkResult.Success)   {
            dialogVisible.value=false
            Toast.makeText(this,"Process successfully completed", Toast.LENGTH_LONG).show()
            onSuccess(data)

        }
        if(response is NetworkResult.Error){
            dialogVisible.value=false
            Log.d(TAG,"Error occurred while attempting to post document data")
            Log.d(TAG, response.toString())
            if (response.code==404)
                throw InvalidCredentialsException("Either the  Public Merchant key is incorrect" +
                        " or the wrong 'dev' argument was supplied")
            else
                throw SdkException(response.message?:"An unexpected error occurred")
        }


    }

    private fun createCameraPermissionRequestLauncher(): ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {granted->

            if (granted){

                cameraPermissionGranted=true


                //show the progress indicator composable
                progressIndicatorVisible.value=true

                if (url!=null){
                    webView.loadUrl(url)
                }else{
                    DocumentCaptureModule.documentActivityObserver.sendDocumentCaptureUrl()
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


}