package co.youverify.workflow_builder_sdk.data

internal data class AccessPointRequest(
    val businessId: String,
    val metadata: Map<String, Any> = emptyMap(),
    val details: Map<String,String> = emptyMap(),
    val mode: String="sdk",
    val templateId: String
)

internal data class AccessPointResponse(
    val data: AccessPointData,
    val links: List<Any>,
    val details: Map<String,String> = emptyMap(),
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
    val faceImage: String="",
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