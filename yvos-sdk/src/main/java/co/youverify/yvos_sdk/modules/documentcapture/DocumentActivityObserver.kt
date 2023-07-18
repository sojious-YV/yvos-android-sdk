package co.youverify.yvos_sdk.modules.documentcapture

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessCheckActivity

internal class DocumentActivityObserver(val documentCaptureModule: DocumentCaptureModule) :DefaultLifecycleObserver {

    private lateinit var documentActivity: DocumentCaptureActivity
    lateinit var option: DocumentOption
    var onSuccessCallback:(String)->Unit={}
     var onCloseCallback:(String)->Unit={}
    var onCancelCallback:(String)->Unit={}
    private var onResumeCalled=false


    override fun onCreate(owner: LifecycleOwner) {
        documentActivity=owner as DocumentCaptureActivity
    }

    override fun onStart(owner: LifecycleOwner) {

        onSuccessCallback=option.onSuccess
        onCloseCallback=option.onClose
        onCancelCallback=option.onCancel

        documentActivity.apply {
            onSuccess=onSuccessCallback
            onClose=onCloseCallback
            onCancel=onCancelCallback
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        //send liveness data only if onResume is being called for the first time and camera permission has been granted
        /*if (!onResumeCalled && documentActivity.cameraPermissionGranted){
            onResumeCalled=true
            documentCaptureModule.sendDocumentCaptureUrl()
        }*/
    }

    fun sendDocumentCaptureUrl(){
        documentCaptureModule.sendDocumentCaptureUrl()
    }
}