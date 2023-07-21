package co.youverify.yvos_sdk.modules.livenesscheck

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import co.youverify.yvos_sdk.modules.vform.FormActivity

internal class LivenessActivityObserver(private val livenessCheckModule: LivenessCheckModule) :DefaultLifecycleObserver {

    private lateinit var livenessActivity: LivenessCheckActivity
    lateinit var option: LivenessOption
    var onSuccessCallback:(LivenessData)->Unit={}
     var onFailureCallback:()->Unit={}
     var onCloseCallback:()->Unit={}
    var onRetryCallback:()->Unit={}
    var onCancelCallback:()->Unit={}
    //private var onResumeCalled=false



    override fun onCreate(owner: LifecycleOwner) {
        livenessActivity=owner as LivenessCheckActivity

    }
    override fun onStart(owner: LifecycleOwner) {


        onSuccessCallback=option.onSuccess
        onFailureCallback=option.onFailure
        onRetryCallback=option.onRetry
        onCloseCallback=option.onClose
        onCancelCallback=option.onCancel

        livenessActivity.apply {
            onSuccess=onSuccessCallback
            onClose=onCloseCallback
            onFailure=onFailureCallback
            onRetry=onRetryCallback
            onCancel=onCancelCallback
        }
    }

    override fun onResume(owner: LifecycleOwner) {

        //send liveness data only if onResume is being called for the first time and camera permission has been granted
       /* if (!onResumeCalled && livenessActivity.cameraPermissionGranted){
            onResumeCalled=true
            livenessCheckModule.sendLivenessUrl()
        }*/
    }

    fun sendLivenessUrl(){
        livenessCheckModule.sendLivenessUrl()
    }
}