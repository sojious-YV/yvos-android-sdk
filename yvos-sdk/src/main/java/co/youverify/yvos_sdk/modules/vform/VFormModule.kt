package co.youverify.yvos_sdk.modules.vform

import android.content.Context
import android.content.Intent
import co.youverify.yvos_sdk.data.AccessPointRequest
import co.youverify.yvos_sdk.data.AccessPointResponse
import co.youverify.yvos_sdk.data.SdkServiceFactory
import co.youverify.yvos_sdk.data.UserDetail
import co.youverify.yvos_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.yvos_sdk.util.PRODUCTION_BASE_URL
import co.youverify.yvos_sdk.util.ID_LENGTH
import co.youverify.yvos_sdk.util.NetworkResult
import co.youverify.yvos_sdk.util.SdkException
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.handleApi

class VFormModule  constructor(private val option: VFormOption) {



    companion object{
         internal lateinit var formActivityObserver: FormActivityObserver
     }

    /**
     * Displays the  vForm associated with this vFormModule
     * Throw SdkException if the provided form Id or public merchant key is invalid or if an attempt is made to display
     * a form created in production environment while setting "dev" option to true and vice-versa
     */
    @Throws(Exception::class)
    suspend fun start(context: Context) {


        //initialize Activity observer
        formActivityObserver=FormActivityObserver()
        formActivityObserver.onSuccessCallback=option.onSuccess
        formActivityObserver.onFailedCallback=option.onFailed
        formActivityObserver.onCompletedCallback=option.onCompleted

        context.startActivity(
            Intent(context, FormActivity::class.java).apply {
                putExtra(USER_NAME,option.personalInfo?.firstName)
            }
        )

        //validate formId length, MerchantKey length and personal info fields
        validateOption()
        
        val personalInfo=option.personalInfo
        val request = AccessPointRequest(
            templateId = option.vFormId,
            businessId = option.publicMerchantKey,
            metadata = option.metadata,
            details = if (personalInfo!=null) UserDetail(
                firstName = personalInfo.firstName,
                lastName = personalInfo.lastName,
                middleName = personalInfo.middleName,
                email = personalInfo.email,
                mobile = personalInfo.mobile,
                gender = personalInfo.gender?.name
            ) else null
        )

        // Get the form accessId
        val response= handleApi { SdkServiceFactory.sdkService(option).getAccessPoint( accessPointRequest = request) }
        handleResponse(response,context)
    }



    private fun handleResponse(response: NetworkResult<AccessPointResponse>, context: Context) {

        if(response is NetworkResult.Success)   { sendFormUrl(response.data.data.id,context) }
        if(response is NetworkResult.Error){
            if (response.code==404)
                throw SdkException("Invalid credentials- Either the form Id or Public Merchant key is incorrect" +
                            " or the wrong 'dev' argument was supplied")
            else
                throw SdkException(response.message?:"An unexpected error occurred")
        }


    }

    private fun sendFormUrl(id: String, context: Context) {

        val baseUrl=if (option.dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
        val formUrl="${baseUrl}/v-forms/${option.vFormId}?accessId=${id}"


        context.startActivity(
            Intent(context, FormActivity::class.java).apply {
                putExtra(URL_TO_DISPLAY,formUrl)
            }
        )
    }

    private fun validateOption() {

        val personalInfo=option.personalInfo

        if (option.vFormId.length!= ID_LENGTH || option.vFormId.isEmpty())
            throw SdkException("vFormId cannot be empty and must be 24 characters long")

        if (option.publicMerchantKey.length!= ID_LENGTH || option.publicMerchantKey.isEmpty())
            throw SdkException("public merchant key cannot be empty and must be 24 characters long")

        if (personalInfo!=null){
            if(
                personalInfo.firstName?.isEmpty()==true || personalInfo.lastName?.isEmpty()==true ||
                personalInfo.middleName?.isEmpty()==true || personalInfo.email?.isEmpty()==true ||
                personalInfo.mobile?.isEmpty()==true
            )
                throw SdkException("Personal Information fields cannot be empty")
        }
        


    }

}