package co.youverify.workflow_builder_sdk.modules.vform

import android.app.Activity
import android.webkit.JavascriptInterface
import co.youverify.workflow_builder_sdk.modules.documentcapture.DocumentCaptureActivity
import co.youverify.workflow_builder_sdk.modules.livenesscheck.LivenessCheckActivity

class JsObject(val presenter: Activity) {
    @JavascriptInterface
    fun receiveMessage(data: String): Boolean {
        if (presenter is FormActivity) presenter.onFormDataReceived(data)
        if (presenter is LivenessCheckActivity) presenter.onLivenessDataReceived(data)
        if (presenter is DocumentCaptureActivity) presenter.onDocumentDataReceived(data)
        //Log.d("Data from JS", data)
        return true
    }
}