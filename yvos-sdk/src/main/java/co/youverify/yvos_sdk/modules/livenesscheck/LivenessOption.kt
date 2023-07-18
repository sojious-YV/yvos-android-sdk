package co.youverify.yvos_sdk.modules.livenesscheck

import android.graphics.Color
import co.youverify.yvos_sdk.Appearance
import co.youverify.yvos_sdk.Option
import co.youverify.yvos_sdk.R


class LivenessOption(
    publicMerchantKey:String,
    dev: Boolean=false,
    val sandBoxEnvironment:Boolean=false,
    metadata: Map<String, Any> = emptyMap(),
    val personalInfo: LivenessPersonalInfo?=null,
    val appearance: Appearance=Appearance(
        greeting = "We will need to carry out a liveness check. It will only take a moment.",
        actionText = "Start Liveness Test",
        buttonBackgroundColor = "#46B2C8",
        buttonTextColor = "#ffffff",
        primaryColor = "#46B2C8"
    ),
    val onSuccess: (LivenessData?) -> Unit={},
    val onFailure: (LivenessData?) -> Unit={},
    val onClose: (LivenessData?) -> Unit={},
    val onCancel: (LivenessData?) -> Unit={},
    val onRetry: (LivenessData?) -> Unit={},
): Option(publicMerchantKey,dev,metadata)

data class LivenessPersonalInfo(val firstName:String="",val lastName:String="")




