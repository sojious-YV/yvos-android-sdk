package co.youverify.yvos_sdk.modules.vform

data class VFormResultData(
    val data: Data?,
    val id: String
)

data class Data(
    val entry: VFormEntryData?
)
data class VFormEntryData(
    val id: String,
    val fields: List<Map<String, Any?>>
)
enum class FormResultType(val id: String) {
    SUCCESS("yvos:vform:submit-success"),
    FAILURE("yvos:vform:submit-failed"),
    COMPLETED("yvos:vform:submit-completed"),
}