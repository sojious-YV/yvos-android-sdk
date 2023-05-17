package co.youverify.workflow_builder_sdk.modules.livenesscheck

data class LivenessResultData(
    val id: String,
    val data: LivenessData?=null
)

data class LivenessData(
    val passed: Boolean,
    val photo: String?
)