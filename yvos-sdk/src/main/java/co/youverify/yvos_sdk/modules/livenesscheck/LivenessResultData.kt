package co.youverify.yvos_sdk.modules.livenesscheck

internal data class LivenessResultData(
    val id: String,
    val data: LivenessData?=null
)

/**
 * Data object that holds the result of a successful liveness check.
 * @property passed indicates the status of the liveness check.
 * @property photo the base64 encoded String of the snapshot taken during the liveness check.
 */
internal data class LivenessData(
    val passed: Boolean,
    val photo: kotlin.String?
)

internal enum class LivenessResultType(val id: kotlin.String) {
    SUCCESS("yvos:liveness:success"),
    FAILED("yvos:liveness:failed"),
    CANCELLED("yvos:liveness:cancelled"),
    CLOSED("yvos:liveness:closed"),
    RETRY("yvos:liveness:retry"),
}