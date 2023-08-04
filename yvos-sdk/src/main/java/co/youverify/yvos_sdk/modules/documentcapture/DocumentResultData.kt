package co.youverify.yvos_sdk.modules.documentcapture


internal data class DocumentResultData(
    val id: String,
    val data: DocumentData?=null
)

/**
 * Data object that holds the result of a successful document capture.
 * @property documentNumber the document number.
 * @property firstName the first name on the document.
 * @property lastName the last name on the document.
 * @property fullName the full name on the document.
 * @property dateOfBirth the date of birth on the document.
 * @property gender the gender of on the document.
 * @property fullDocumentImage the base64 encoded String of the full document Image (front and back).
 * @property fullDocumentFrontImage the base64 encoded String of the front Image of the document.
 * @property fullDocumentBackImage the base64 encoded String of the back Image of the document.
 * @property rawMRZString the raw String representation of the document.
 */
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


internal enum class DocumentResultType(val id: String) {
    SUCCESS("yvos:document:success"),
    CANCELLED("yvos:document:cancelled"),
    CLOSED("yvos:document:closed"),
}

internal data class CountryData(
    val countryCode: String,
    val idTypes: List<String>,
    val province: List<String>
)