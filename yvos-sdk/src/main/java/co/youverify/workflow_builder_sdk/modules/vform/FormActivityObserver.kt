package co.youverify.workflow_builder_sdk.modules.vform

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class FormActivityObserver:DefaultLifecycleObserver {

     var onSuccessCallback:(VFormResultData)->Unit={}
     var onCloseCallback:()->Unit={}

    override fun onStart(owner: LifecycleOwner) {
        val activity=owner as FormActivity
        activity.apply {
            onSuccess=onSuccessCallback
            onClose=onCloseCallback
        }
    }
}