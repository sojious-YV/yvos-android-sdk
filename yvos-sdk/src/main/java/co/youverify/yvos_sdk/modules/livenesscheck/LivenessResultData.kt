package co.youverify.yvos_sdk.modules.livenesscheck

data class LivenessResultData(
    val id: String,
    val data: LivenessData?=null
)

data class LivenessData(
    val passed: Boolean,
    val photo: String?
)

enum class LivenessResultType(val id: String) {
    SUCCESS("yvos:liveness:success"),
    FAILED("yvos:liveness:failed"),
    CANCELLED("yvos:liveness:cancelled"),
    CLOSED("yvos:liveness:closed"),
    RETRY("yvos:liveness:retry"),
}