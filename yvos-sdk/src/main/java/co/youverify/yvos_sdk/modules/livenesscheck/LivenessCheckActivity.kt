package co.youverify.yvos_sdk.modules.livenesscheck

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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import co.youverify.yvos_sdk.R
import co.youverify.yvos_sdk.components.ModalWindow
import co.youverify.yvos_sdk.components.ProgressIndicator
import co.youverify.yvos_sdk.data.LivenessRequest
import co.youverify.yvos_sdk.data.LivenessResponse
import co.youverify.yvos_sdk.data.SdkServiceFactory
import co.youverify.yvos_sdk.JsObject
import co.youverify.yvos_sdk.modules.vform.VFormModule
import co.youverify.yvos_sdk.theme.SdkTheme
import co.youverify.yvos_sdk.util.NetworkResult
import co.youverify.yvos_sdk.util.SdkException
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.handleApi
import com.google.gson.Gson
import java.io.IOException

class LivenessCheckActivity : AppCompatActivity() {

    private val progressIndicatorVisible= mutableStateOf(true)
    var cameraPermissionGranted = false
    private set
    private lateinit var modalWindowView: ComposeView
    private lateinit var progressIndicatorView: ComposeView
    private var userName: String?=null
    private var pageReady = false
    private  var url: String?=null
    private lateinit var webView: WebView
    private val TAG="FormActivity"
    private val modalWindowVisible = mutableStateOf(false)
    private val cameraPermissionRequestLauncher: ActivityResultLauncher<String> = createCameraPermissionRequestLauncher()
    lateinit var onClose:(LivenessData?)->Unit
    lateinit var onFailure:(LivenessData?)->Unit
    lateinit var onSuccess:(LivenessData?)->Unit
    lateinit var onRetry:(LivenessData?)->Unit
    lateinit var onCancel:(LivenessData?)->Unit



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liveness_check)

        lifecycle.addObserver(LivenessCheckModule.livenessActivityObserver)

        initializeViews()
        setUpWebView()

        //Disable back button
        /*onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                return
                //if (pageReady) return else finish()
            }
        })*/

    }

    override fun onStart() {
        super.onStart()
        checkForCameraPermission()
    }

    private fun initializeViews() {

        webView=findViewById(R.id.webView_liveness)
        modalWindowView=findViewById(R.id.modalWindowView)
        //closeButton=findViewById(R.id.liveness_close_button)
        progressIndicatorView=findViewById(R.id.progressIndicatorView)
        /*closeButton.setOnClickListener{
            finish()
        }*/
        val appearance=LivenessCheckModule.livenessActivityObserver.option.appearance


        userName= intent.getStringExtra(USER_NAME)

        progressIndicatorView.setContent {
            ProgressIndicator(colorString =appearance.primaryColor , visible =progressIndicatorVisible.value )
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
                    buttonBackGroundColorString = appearance.buttonBackgroundColor,
                    buttonTextColorString = appearance.buttonTextColor,
                    buttonText = appearance.actionText,
                    greeting = appearance.greeting
                )
            }

        }
    }

    private fun checkForCameraPermission() {

        if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            cameraPermissionGranted=true

            //load url if url it's not presently null otherwise call LivenessCheckModule class to send it
            if (url!=null){
                webView.loadUrl(url!!)
            }else{
                LivenessCheckModule.livenessActivityObserver.sendLivenessUrl()
            }

        }else{
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
            //settings.builtInZoomControls=true
            settings.displayZoomControls=false
            settings.mediaPlaybackRequiresUserGesture=false
            addJavascriptInterface(JsObject(this@LivenessCheckActivity),"Android")
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
        if (cameraPermissionGranted) webView.loadUrl(url!!)

    }

    fun onLivenessDataReceived(data: String) {

        //If the browser event received does not contain Liveness Data
        if (data.contains("origin")){
            return
        }


        val livenessResultData=Gson().fromJson(data,LivenessResultData::class.java)

        if(data.contains(LivenessResultType.FAILED.id) ){
            //closeButton.visibility=View.INVISIBLE
            Log.d("LivenessActivity",data)
            //lifecycleScope.launch { delay(500) }
            onFailure(livenessResultData.data)
            return

            //finish()
        }


        if (data.contains(LivenessResultType.SUCCESS.id)) {
            Log.d("LivenessActivity",data)
            //closeButton.visibility=View.INVISIBLE
            onSuccess(livenessResultData.data)
            //progressBar.visibility=View.VISIBLE
            /*lifecycleScope.launch {
                postLivenessData(livenessResultData.data)
            }*/
            return
        }



        if(data.contains(LivenessResultType.CANCELLED.id)) {
            Log.d("LivenessActivity",data)
            //closeButton.visibility=View.VISIBLE
            onCancel(livenessResultData.data)
            return

        }

            if(data.contains(LivenessResultType.CLOSED.id)){
                Log.d("LivenessActivity",data)
                //closeButton.visibility=View.VISIBLE
                onClose(livenessResultData.data)
                return
                //finish()
            }
            if (data.contains(LivenessResultType.RETRY.id)){
                Log.d("LivenessActivity",data)
                //closeButton.visibility=View.VISIBLE
                onRetry(livenessResultData.data)
                return
                //finish()
            }
        }


    private suspend fun postLivenessData(data: LivenessData?) {

        val option=LivenessCheckModule.livenessActivityObserver.option

        val request = LivenessRequest(
            publicMerchantID =option.publicMerchantKey ,
            faceImage = data?.photo!!,
            passed = data.passed,
            metadata = option.metadata
        )

        val response:NetworkResult<LivenessResponse>

        try {
            response=handleApi { SdkServiceFactory.sdkService(option).postLivenessData( livenessRequest = request) }
            handleResponse(response)
        }catch (e:IOException){

            Snackbar.make(
                webView.rootView,
                "Could not connect to server, check your internet connection",
                Snackbar.LENGTH_INDEFINITE
            )
                .show()
        }
    }


    private fun handleResponse(response: NetworkResult<LivenessResponse>) {

        if(response is NetworkResult.Success)   {

            onSuccess(LivenessData(passed = response.data.data.passed,photo = response.data.data.faceImage))
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

                //show the progress indicator composable
                progressIndicatorVisible.value=true

                if (url!=null){
                    webView.loadUrl(url!!)
                }else{
                    LivenessCheckModule.livenessActivityObserver.sendLivenessUrl()
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
}


