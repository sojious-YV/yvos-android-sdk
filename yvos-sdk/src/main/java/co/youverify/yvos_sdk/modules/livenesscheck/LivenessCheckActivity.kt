package co.youverify.yvos_sdk.modules.livenesscheck

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.lifecycleScope
import co.youverify.yvos_sdk.R
import co.youverify.yvos_sdk.components.ModalWindow
import co.youverify.yvos_sdk.data.LivenessRequest
import co.youverify.yvos_sdk.data.LivenessResponse
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

class LivenessCheckActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private var cameraPermissionGranted = false
    private lateinit var composeView: ComposeView
    private var userName: String?=null
    private var pageReady = false
    private  var url: String?=null
    private lateinit var webView: WebView
    private val TAG="FormActivity"
    //private lateinit var closeButton: ImageView
    private lateinit var progressBar: ProgressBar
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



        initializeViews()
        setUpWebView()
        checkForCameraPermission()

        //Disable back button
        /*onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                return
                //if (pageReady) return else finish()
            }
        })*/

        lifecycle.addObserver(LivenessCheckModule.livenessActivityObserver)


    }



    private fun initializeViews() {

        webView=findViewById(R.id.webView_liveness)
        composeView=findViewById(R.id.composeView)
        //closeButton=findViewById(R.id.liveness_close_button)
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

        if(checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            cameraPermissionGranted=true

            //load url if url is not presently null i.e if it has been sent by LivenessCheckModule class
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
            addJavascriptInterface(JsObject(this@LivenessCheckActivity),"Android")
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
            progressBar.visibility=View.INVISIBLE
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
            progressBar.visibility=View.INVISIBLE
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
                url?.let{
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


