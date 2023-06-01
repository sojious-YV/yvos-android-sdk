package co.youverify.yvos_sdk.modules.vform

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class FormActivityObserver:DefaultLifecycleObserver {

     var onSuccessCallback:(VFormEntryData?)->Unit={}
     var onFailedCallback:(VFormEntryData?)->Unit={}
    var onCompletedCallback:(VFormEntryData?)->Unit={}

    override fun onStart(owner: LifecycleOwner) {
        val activity=owner as FormActivity
        activity.apply {
            onSuccess=onSuccessCallback
            onFailed=onFailedCallback
            onCompleted=onCompletedCallback
        }
    }
}