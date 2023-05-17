package co.youverify.workflow_builder_sdk.modules.documentcapture

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class DocumentActivityObserver:DefaultLifecycleObserver {

    lateinit var option: DocumentOption
    var onSuccessCallback:()->Unit={}
     var onCloseCallback:(DocumentResultData)->Unit={}
    var onCancelCallback:(DocumentResultData)->Unit={}

    override fun onStart(owner: LifecycleOwner) {
        val activity=owner as DocumentCaptureActivity
        activity.apply {
            onSuccess=onSuccessCallback
            onClose=onCloseCallback
            onCancel=onCancelCallback
        }
    }
}