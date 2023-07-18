package co.youverify.yvos_sdk.modules.documentcapture


data class DocumentResultData(
    val id: String,
    val data: DocumentData?=null
)


data class DocumentData(
    val documentNumber: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val dateOfBirth: String,
    val dateOfExpiry: String,
    val gender: String,
    val rawMRZString: String,
    val fullDocumentFrontImage: String,
    val fullDocumentBackImage: String?,
    val fullDocumentImage: String?
)


enum class DocumentResultType(val id: String) {
    SUCCESS("yvos:document:success"),
    CANCELLED("yvos:document:cancelled"),
    CLOSED("yvos:document:closed"),
}

data class CountryData(
    val countryCode: String,
    val idTypes: List<String>,
    val province: List<String>
)