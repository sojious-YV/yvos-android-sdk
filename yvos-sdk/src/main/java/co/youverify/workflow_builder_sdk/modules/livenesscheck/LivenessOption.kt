package co.youverify.workflow_builder_sdk.modules.livenesscheck

import co.youverify.workflow_builder_sdk.Option



 class LivenessOption(
    publicMerchantKey:String,
    dev: Boolean=false,
    metadata: Map<String, Any> = emptyMap(),
    val personalInfo: LivenessPersonalInfo?=null,
    val onSuccess: (LivenessResultData) -> Unit={},
    val onFailure: (LivenessResultData) -> Unit={},
    val onClose: (LivenessResultData) -> Unit={},
    val onCancel: (LivenessResultData) -> Unit={},
    val onRetry: (LivenessResultData) -> Unit={},
): Option(publicMerchantKey,dev,metadata)

data class LivenessPersonalInfo(val firstName:String="",val lastName:String="")



