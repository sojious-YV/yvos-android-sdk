package co.youverify.yvos_sdk.data

internal data class AccessPointRequest(
    val businessId: String,
    val metadata: Map<String, Any> = emptyMap(),
    val details: UserDetail?,
    val mode: String ="sdk",
    val templateId: String
)


data class UserDetail(
    val firstName: String?,
    val lastName: String?,
    val middleName: String?,
    val email: String?,
    val mobile: String?,
    val gender: String?
)

internal data class AccessPointResponse(
    val data: AccessPointData,
    val links: List<Any>,
    val details: Map<String, String> = emptyMap(),
    val message: String,
    val statusCode: Int,
    val success: Boolean
)

internal data class AccessPointData(
    val _createdAt: String,
    val _lastModifiedAt: String,
    val createdAt: String,
    val creatorId: Any,
    val details: Any,
    val id: String,
    val lastModifiedAt: String,
    val mode: String,
    val templateId: String
)

internal data class LivenessRequest(
    val publicMerchantID: String,
    val faceImage: String ="",
    val passed: Boolean,
    val metadata: Map<String, Any> = emptyMap()
){
    val components = listOf("liveness")
}

internal data class LivenessResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: LivenessData,
    val links: List<Any>
)

internal data class LivenessData(
    val parentId: Any?,
    val method: String,
    val components: List<String>,
    val faceImage: String,
    val passed: Boolean,
    val country: Any?,
    val isConsent: Boolean,
    val metadata: Map<String, Any>,
    val businessId: String,
    val requestedAt: String,
    val createdAt: String,
    val lastModifiedAt: String,
    val _createdAt: String,
    val _lastModifiedAt: String,
    val id: String
)

data class DocumentRequest(
    val publicMerchantID: String,
    val components: List<String> = listOf("id_capture"),
    val documentType: String,
    val method: String ="documentCapture",
    val documentNumber: String,
    val metadata: Map<String, Any> = emptyMap()
)
data class DocumentResponse(
    val licenseDetails: LicenseDetails?,
    val firstName: String?,
    val middleName: String?,
    val lastName: String?,
    val status: String,
    val fullName: String?,
    val components: List<String>,
    val dateOfBirth: String?,
    val dateOfExpiry: String?,
    val gender: String?,
    val rawMRZString: String?,
    val fullDocumentFrontImage: String?,
    val fullDocumentBackImage: String?,
    val faceImage: String?,
    val address: String?,
    val issuingAuthority: String?,
    val maritalStatus: String?,
    val placeOfBirth: String?,
    val signatureImage: String?,
    val dateOfIssue: String?,
    val country: String?,
    val notifyWhenIdExpire: Boolean,
    val isConsent: Boolean,
    val documentType: String,
    val method: String,
    val documentNumber: String,
    val metadata: Map<String, Any>,
    val businessId: String,
    val requestedAt: String,
    val createdAt: String,
    val lastModifiedAt: String,
    val _createdAt: String,
    val _lastModifiedAt: String,
    val id: String,
    val links: List<Any>
)

data class LicenseDetails(
    val vehicleClass: String?,
    val conditions: String?,
    val endorsements: String?
)
