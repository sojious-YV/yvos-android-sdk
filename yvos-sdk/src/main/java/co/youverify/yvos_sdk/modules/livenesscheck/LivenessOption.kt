package co.youverify.yvos_sdk.modules.livenesscheck

import co.youverify.yvos_sdk.Option



 class LivenessOption(
    publicMerchantKey:String,
    dev: Boolean=false,
    metadata: Map<String, Any> = emptyMap(),
    val personalInfo: LivenessPersonalInfo?=null,
    val onSuccess: (LivenessData?) -> Unit={},
    val onFailure: (LivenessData?) -> Unit={},
    val onClose: (LivenessData?) -> Unit={},
    val onCancel: (LivenessData?) -> Unit={},
    val onRetry: (LivenessData?) -> Unit={},
): Option(publicMerchantKey,dev,metadata)

data class LivenessPersonalInfo(val firstName:String="",val lastName:String="")



