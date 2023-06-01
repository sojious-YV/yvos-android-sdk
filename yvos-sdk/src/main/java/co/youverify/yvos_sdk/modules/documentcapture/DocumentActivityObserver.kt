package co.youverify.yvos_sdk.modules.documentcapture

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class DocumentActivityObserver(val documentCaptureModule: DocumentCaptureModule) :DefaultLifecycleObserver {

    lateinit var option: DocumentOption
    var onSuccessCallback:(DocumentData?)->Unit={}
     var onCloseCallback:(DocumentData?)->Unit={}
    var onCancelCallback:(DocumentData?)->Unit={}
    private var onResumeCalled=false

    override fun onStart(owner: LifecycleOwner) {

        onSuccessCallback=option.onSuccess
        onCloseCallback=option.onClose
        onCancelCallback=option.onCancel

        val activity=owner as DocumentCaptureActivity
        activity.apply {
            onSuccess=onSuccessCallback
            onClose=onCloseCallback
            onCancel=onCancelCallback
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        //send liveness data only if onResume is being called for the first time
        if (!onResumeCalled){
            onResumeCalled=true
            documentCaptureModule.sendDocumentCaptureUrl()
        }
    }
}