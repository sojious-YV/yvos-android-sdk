package co.youverify.workflow_builder_sdk.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface SdkService {


        @POST("v-forms/access-points/")
        suspend fun getAccessPoint( @Body accessPointRequest: AccessPointRequest): Response<AccessPointResponse>

        @POST("v1/sdk/identity/liveness/")
        suspend fun postLivenessData(@Body livenessRequest: LivenessRequest): Response<LivenessResponse>

}