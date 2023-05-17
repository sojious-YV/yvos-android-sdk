package co.youverify.workflow_builder_sdk.modules.documentcapture

import co.youverify.workflow_builder_sdk.Option

 class DocumentOption(

    publicMerchantKey:String,
    dev: Boolean=false,
    metadata: Map<String, Any> = emptyMap(),
    personalInfo:DocumentPersonalInfo?=null,
    val countries: List<Country>?=null,
    val onSuccess: () -> Unit,
    val onCancel: (DocumentResultData) -> Unit,
    val onClose: (DocumentResultData) -> Unit,

    ): Option(publicMerchantKey,dev,metadata)

 data class Country(
    val countryCode: String="",
    val idTypes: List<String> = emptyList(),
    val province: List<String> = emptyList()
)

data class DocumentPersonalInfo (val firstName:String="")
enum class IdType(name: String) {
   National_ID("National_ID"),
   Passport("Passport"),
   Driver_LICENSE("Driver License"),
   VOTERS_CARD("Voters Card"),
   PERMIT_CARD("Permit Card")
}

data class DocumentResultData(
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