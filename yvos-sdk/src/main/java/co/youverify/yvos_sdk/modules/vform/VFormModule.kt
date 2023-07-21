package co.youverify.yvos_sdk.modules.vform

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import co.youverify.yvos_sdk.data.AccessPointRequest
import co.youverify.yvos_sdk.data.AccessPointResponse
import co.youverify.yvos_sdk.data.SdkServiceFactory
import co.youverify.yvos_sdk.data.UserDetail
import co.youverify.yvos_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.yvos_sdk.util.PRODUCTION_BASE_URL
import co.youverify.yvos_sdk.util.ID_LENGTH
import co.youverify.yvos_sdk.exceptions.InvalidArgumentException
import co.youverify.yvos_sdk.exceptions.InvalidCredentialsException
import co.youverify.yvos_sdk.exceptions.SdkException
import co.youverify.yvos_sdk.util.NetworkResult
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.handleApi
import co.youverify.yvos_sdk.util.validatePublicMerchantKeyAndAppearance

/**
 * This class enables access to the Vform service.
 * @property option holds the appearance specifications and the information needed to display the appropriate form.
 * @constructor creates an instant of this module whose configuration is specified by the "option" property.
 */
class VFormModule  constructor(private val option: VFormOption) {


    private lateinit var mContext: Context

    companion object{
         internal lateinit var formActivityObserver: FormActivityObserver
     }

    /**
     * Starts the vForm service by attempting to display the form that corresponds to the "formId" supplied in the "option" property.
     * @throws InvalidCredentialsException if the "publicMerchantKey" specified in the option is invalid or if the wrong value was supplied for the "dev" argument.
     * @throws InvalidArgumentException if an invalid color string was supplied in the "appearance" configuration.
     * @param context the context object to be passed.
     */

    fun start(context: Context) {


        //initialize Activity observer
        formActivityObserver=FormActivityObserver(this)
        formActivityObserver.option=option
        mContext=context


        context.startActivity(
            Intent(context, FormActivity::class.java).apply {
                putExtra(USER_NAME,option.personalInfo?.firstName)
            }
        )

        //validate formId length, MerchantKey length and personal info fields
        validateOption()
    }


    suspend fun sendFormUrl() {


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
                gender = personalInfo.gender.name
            ) else null
        )

        // Get the form accessId
        val response= handleApi { SdkServiceFactory.sdkService(option).getAccessPoint( accessPointRequest = request) }
        handleResponse(response,mContext)


    }

    private fun handleResponse(response: NetworkResult<AccessPointResponse>, context: Context) {

        if(response is NetworkResult.Success)   {
            //sendFormUrl(response.data.data.id,context)
            val accessId=response.data.data.id
            Log.d("FormActivity",accessId)
            val baseUrl=if (option.dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
            val formUrl=if(option.sandBoxEnvironment) "${baseUrl}/v-forms/${option.vFormId}?accessId=${accessId}&e=s"
            else "${baseUrl}/v-forms/${option.vFormId}?accessId=${accessId}"


            context.startActivity(
                Intent(context, FormActivity::class.java).apply {
                    putExtra(URL_TO_DISPLAY,formUrl)
                }
            )
        }

        if(response is NetworkResult.Error){
            if (response.code==404)
                throw InvalidCredentialsException("Invalid credentials- Either the form Id or Public Merchant key is incorrect" +
                        " or the wrong 'dev' argument was supplied")
            else
                throw SdkException(response.message?:"An unexpected error occurred")
        }

        if(response is NetworkResult.Exception)   {
            Toast.makeText(context,response.genericMessage,Toast.LENGTH_LONG).show()
        }


    }

    private fun validateOption() {

        val personalInfo=option.personalInfo

        validatePublicMerchantKeyAndAppearance(
            publicMerchantKey = option.publicMerchantKey,
            appearance = option.appearance
        )

        if (option.vFormId.length!= ID_LENGTH || option.vFormId.isEmpty())
            throw InvalidCredentialsException("vFormId cannot be empty and must be 24 characters long")


        if (personalInfo!=null){
            if(
                personalInfo.firstName?.isEmpty()==true || personalInfo.lastName?.isEmpty()==true ||
                personalInfo.middleName?.isEmpty()==true || personalInfo.email?.isEmpty()==true ||
                personalInfo.mobile?.isEmpty()==true
            )
                throw InvalidArgumentException("Personal Information fields cannot be empty")
        }
        


    }

}