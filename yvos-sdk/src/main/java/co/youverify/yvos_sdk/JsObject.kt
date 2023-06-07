package co.youverify.yvos_sdk

import android.app.Activity
import android.webkit.JavascriptInterface
import co.youverify.yvos_sdk.modules.documentcapture.DocumentCaptureActivity
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessCheckActivity
import co.youverify.yvos_sdk.modules.vform.FormActivity

internal class JsObject(private val presenter: Activity) {
    @JavascriptInterface
    fun receiveMessage(data: String): Boolean {
        if (presenter is FormActivity) presenter.onFormDataReceived(data)
        if (presenter is LivenessCheckActivity) presenter.onLivenessDataReceived(data)
        if (presenter is DocumentCaptureActivity) presenter.onDocumentDataReceived(data)
        //Log.d("Data from JS", data)
        return true
    }
}