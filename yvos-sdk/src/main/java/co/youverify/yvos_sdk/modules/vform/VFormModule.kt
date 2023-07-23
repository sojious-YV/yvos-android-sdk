package co.youverify.yvos_sdk.modules.vform

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import co.youverify.yvos_sdk.Customization
import co.youverify.yvos_sdk.SdkModule
import co.youverify.yvos_sdk.UserInfo
import co.youverify.yvos_sdk.data.AccessPointRequest
import co.youverify.yvos_sdk.data.AccessPointResponse
import co.youverify.yvos_sdk.data.SdkServiceFactory
import co.youverify.yvos_sdk.data.UserDetail
import co.youverify.yvos_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.yvos_sdk.util.PRODUCTION_BASE_URL
import co.youverify.yvos_sdk.util.ID_LENGTH
import co.youverify.yvos_sdk.exceptions.InvalidCredentialsException
import co.youverify.yvos_sdk.exceptions.SdkException
import co.youverify.yvos_sdk.util.FINISH_ACTIVITY
import co.youverify.yvos_sdk.util.NetworkResult
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.handleApi
import co.youverify.yvos_sdk.util.validatePublicMerchantKeyAndAppearance

/**
 * This class enables access to the Vform service.
 * @param builder object used to define both mandatory and optional parameters.
 * @constructor creates an instant of [VFormModule] using the passed in builder.
 */
class VFormModule private constructor(builder: Builder): SdkModule(
    builder.publicMerchantKey,
    builder.dev,builder.customization,
    builder.userInfo,
    builder.metaData,
) {


    private lateinit var mContext: Context
    var vFormId: String
        private set
    var onSuccess: (String) -> Unit
        private set
    var onFailed: () -> Unit
        private set
    var onCompleted: (String) -> Unit
        private set
    var mMetaData: Map<String,Any>
        private set
    var sandBoxEnvironment = false
    private set

    init {
        this.vFormId = builder.vFormId
        this.onSuccess = builder.onSuccess
        this.onFailed = builder.onFailed
        this.onCompleted = builder.onCompleted
        this.mMetaData = builder.metaData
    }



    companion object{
         internal lateinit var formActivityObserver: FormActivityObserver

     }


    class Builder(publicMerchantKey:String,formId:String){
        //private lateinit var mContext: Context
        var vFormId: String
        var publicMerchantKey: String
        var dev:Boolean = false
        var customization:Customization = Customization.Builder().build()
        private set
        var userInfo:UserInfo? = null
        var onSuccess: (String) -> Unit = {}
        private set
        var onFailed: () -> Unit={}
        private set
        var onCompleted: (String) -> Unit={}
        private set
        var metaData: Map<String,Any> = emptyMap()


        init {
            this.vFormId=formId
            this.publicMerchantKey = publicMerchantKey
        }
        fun dev(dev:Boolean): Builder {
            this.dev = dev
            return this
        }
        fun onSuccess (onSuccess:(String) -> Unit): Builder {
            this.onSuccess = onSuccess
            return this

        }

        fun onFailed(onFailed:() -> Unit): Builder {
            this.onFailed = onFailed
            return this

        }

        fun onCompleted(onCompleted:(String) -> Unit): Builder {
            this.onCompleted = onCompleted
            return this

        }

        fun customization(customization:Customization): Builder {
            this.customization = customization
            return this
        }

        fun userInfo(userInfo: UserInfo?): Builder {
            this.userInfo = userInfo
            return this

        }

        fun metaData(metaData: Map<String,Any>): Builder {
            this.metaData = metaData
            return this
        }

        fun build(): VFormModule {
            return VFormModule(this)
        }
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

        mContext=context


        context.startActivity(
            Intent(context, FormActivity::class.java).apply {
                putExtra(USER_NAME, userInfo?.firstName)
            }
        )

        //validate formId length, MerchantKey length and personal info fields
        validateProperties()
    }


   internal suspend fun sendFormUrl() {


        val personalInfo=userInfo
        val request = AccessPointRequest(
            templateId = vFormId,
            businessId = publicMerchantKey,
            metadata = mMetaData,
            details = if (userInfo!=null) UserDetail(
                firstName = userInfo?.firstName,
                lastName = personalInfo?.lastName,
                middleName = personalInfo?.middleName,
                email = personalInfo?.email,
                mobile = personalInfo?.mobile,
                gender = personalInfo?.gender?.name
            ) else null
        )

        // Get the form accessId
        val response= handleApi { SdkServiceFactory.sdkService(this).getAccessPoint( accessPointRequest = request) }
        handleResponse(response,mContext)


    }

    private fun handleResponse(response: NetworkResult<AccessPointResponse>, context: Context) {

        if(response is NetworkResult.Success)   {
            //sendFormUrl(response.data.data.id,context)
            val accessId=response.data.data.id
            Log.d("FormActivity",accessId)
            val baseUrl=if (dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
            val formUrl=if(sandBoxEnvironment) "${baseUrl}/v-forms/${vFormId}?accessId=${accessId}&e=s"
            else "${baseUrl}/v-forms/${vFormId}?accessId=${accessId}"


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

    private fun validateProperties() {

        //val personalInfo=userInfo

        validatePublicMerchantKeyAndAppearance(
            publicMerchantKey = publicMerchantKey,
            appearance = customization
        )

        if (vFormId.length!= ID_LENGTH || vFormId.isEmpty())
            throw IllegalArgumentException("vFormId cannot be empty and must be 24 characters long")


        if (userInfo!=null){
            if(
                userInfo?.firstName?.isEmpty()==true || userInfo?.lastName?.isEmpty()==true ||
                userInfo?.middleName?.isEmpty()==true || userInfo?.email?.isEmpty()==true ||
                userInfo?.mobile?.isEmpty()==true
            )
                throw IllegalArgumentException("Personal Information fields cannot be empty")
        }

    }

    fun close(){
        mContext.startActivity(
            Intent(mContext, FormActivity::class.java).apply {
                putExtra(FINISH_ACTIVITY,true)
            }
        )

    }

}