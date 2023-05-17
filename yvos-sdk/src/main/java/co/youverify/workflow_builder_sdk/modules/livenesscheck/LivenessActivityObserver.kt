package co.youverify.workflow_builder_sdk.modules.livenesscheck

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class LivenessActivityObserver:DefaultLifecycleObserver {

    lateinit var option: LivenessOption
    var onSuccessCallback:(LivenessResultData)->Unit={}
     var onFailureCallback:(LivenessResultData)->Unit={}
     var onCloseCallback:(LivenessResultData)->Unit={}
    var onRetryCallback:(LivenessResultData)->Unit={}
    var onCancelCallback:(LivenessResultData)->Unit={}

    override fun onStart(owner: LifecycleOwner) {
        val activity=owner as LivenessCheckActivity
        activity.apply {
            onSuccess=onSuccessCallback
            onClose=onCloseCallback
            onFailure=onFailureCallback
            onRetry=onRetryCallback
            onCancel=onCancelCallback
        }
    }
}