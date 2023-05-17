package co.youverify.workflow_builder_sdk.modules.vform

import android.content.Context
import android.content.Intent
import co.youverify.workflow_builder_sdk.data.AccessPointRequest
import co.youverify.workflow_builder_sdk.data.AccessPointResponse
import co.youverify.workflow_builder_sdk.data.SdkServiceFactory
import co.youverify.workflow_builder_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.workflow_builder_sdk.util.PRODUCTION_BASE_URL
import co.youverify.workflow_builder_sdk.util.ID_LENGTH
import co.youverify.workflow_builder_sdk.util.NetworkResult
import co.youverify.workflow_builder_sdk.util.SdkException
import co.youverify.workflow_builder_sdk.util.URL_TO_DISPLAY
import co.youverify.workflow_builder_sdk.util.handleApi

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



        //validate formId and MerchantKey length
        validateOption()

        //Create  AppComponent instance
        //DaggerAppComponent.factory().create().inject(this)

        val request = AccessPointRequest(
            templateId = option.vFormId,
            businessId = option.publicMerchantKey,
            metadata = option.metadata,
        )



        val response= handleApi { SdkServiceFactory.sdkService(option).getAccessPoint( accessPointRequest = request) }
        handleResponse(response,context)
    }



    private fun handleResponse(response: NetworkResult<AccessPointResponse>, context: Context) {

        if(response is NetworkResult.Success)   { displayForm(response.data.data.id,context) }
        if(response is NetworkResult.Error){
            if (response.code==404)
                throw SdkException("Invalid credentials- Either the form Id or Public Merchant key is incorrect" +
                            " or the wrong 'dev' argument was supplied")
            else
                throw SdkException(response.message?:"An unexpected error occurred")
        }


    }

    private fun displayForm(id: String, context: Context) {

        val baseUrl=if (option.dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
        val formUrl="${baseUrl}/v-forms/${option.vFormId}?accessId=${id}"

        formActivityObserver=FormActivityObserver()
        formActivityObserver.onSuccessCallback=option.onSuccess
        formActivityObserver.onCloseCallback=option.onClose

        context.startActivity(
            Intent(context, FormActivity::class.java).apply {
                putExtra(URL_TO_DISPLAY,formUrl)
            }
        )
    }

    private fun validateOption() {
        if (option.vFormId.length!= ID_LENGTH || option.vFormId.isEmpty())
            throw SdkException("vFormId cannot be empty and must be 24 characters long")

        if (option.publicMerchantKey.length!= ID_LENGTH || option.publicMerchantKey.isEmpty())
            throw SdkException("public merchant key cannot be empty and must be 24 characters long")
    }

}