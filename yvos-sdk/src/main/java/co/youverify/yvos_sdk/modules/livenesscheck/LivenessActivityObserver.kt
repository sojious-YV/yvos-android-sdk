package co.youverify.yvos_sdk.modules.livenesscheck

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class LivenessActivityObserver(val livenessCheckModule: LivenessCheckModule) :DefaultLifecycleObserver {

    lateinit var option: LivenessOption
    var onSuccessCallback:(LivenessData?)->Unit={}
     var onFailureCallback:(LivenessData?)->Unit={}
     var onCloseCallback:(LivenessData?)->Unit={}
    var onRetryCallback:(LivenessData?)->Unit={}
    var onCancelCallback:(LivenessData?)->Unit={}
    private var onResumeCalled=false

    override fun onStart(owner: LifecycleOwner) {


        onSuccessCallback=option.onSuccess
        onFailureCallback=option.onFailure
        onRetryCallback=option.onRetry
        onCloseCallback=option.onClose
        onCancelCallback=option.onCancel

        val activity=owner as LivenessCheckActivity
        activity.apply {
            onSuccess=onSuccessCallback
            onClose=onCloseCallback
            onFailure=onFailureCallback
            onRetry=onRetryCallback
            onCancel=onCancelCallback
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        //send liveness data only if onResume is being called for the first time
        if (!onResumeCalled){
            onResumeCalled=true
            livenessCheckModule.sendLivenessUrl()
        }
    }
}