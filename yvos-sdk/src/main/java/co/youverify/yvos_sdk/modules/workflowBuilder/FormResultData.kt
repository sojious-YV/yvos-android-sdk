package co.youverify.yvos_sdk.modules.workflowBuilder

internal data class FormResultData(
    val data: Data?,
    val id: String
)

internal data class Data(
    val entry: FormEntry?
)
internal data class FormEntry(
    val id: String,
    val fields: List<Map<String, Any?>>
)
internal enum class FormResultType(val id:String) {
    SUCCESS("yvos:vform:submit-success"),
    FAILURE("yvos:vform:submit-failed"),
    COMPLETED("yvos:vform:submit-completed"),
}