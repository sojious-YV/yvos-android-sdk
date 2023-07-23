package co.youverify.yvos_sdk.modules.livenesscheck

import android.content.Context
import android.content.Intent
import co.youverify.yvos_sdk.Customization
import co.youverify.yvos_sdk.SdkModule
import co.youverify.yvos_sdk.UserInfo
import co.youverify.yvos_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.yvos_sdk.exceptions.InvalidCredentialsException
import co.youverify.yvos_sdk.util.FINISH_ACTIVITY
import co.youverify.yvos_sdk.util.PRODUCTION_BASE_URL
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.validatePublicMerchantKeyAndAppearance
/**
 * This class enables access to the Liveness service.
 * @param builder object used to define both mandatory and optional parameters.
 * @constructor creates an instant of [LivenessCheckModule] using the passed in builder.
 */
class LivenessCheckModule  constructor(builder:Builder): SdkModule(
    builder.publicMerchantKey,
    builder.dev,builder.customization,
    builder.userInfo,
    builder.metaData,
){


    private lateinit var mContext: Context

    var onSuccess: (String) -> Unit
        private set
    var onFailed: () -> Unit
        private set
    var onCancel: () -> Unit
        private set
    var onRetry: () -> Unit
        private set
    var onClose: () -> Unit
        private set
    var sandBoxEnvironment = false

    init {
        this.onSuccess = builder.onSuccess
        this.onFailed = builder.onFailed
        this.onCancel = builder.onCancel
        this.onRetry = builder.onRetry
        this.onClose = builder.onClose
    }

    companion object{
         internal lateinit var livenessActivityObserver: LivenessActivityObserver
     }

    class Builder(publicMerchantKey: kotlin.String){

        var publicMerchantKey: kotlin.String
        var dev:Boolean = false
        var customization:Customization = Customization.Builder().build()
            private set
        var userInfo:UserInfo? = null
        var onSuccess: (String) -> Unit = {}
            private set
        var onFailed: () -> Unit={}
            private set
        var onCancel: () -> Unit = {}
            private set
        var onRetry: () -> Unit={}
            private set

        var onClose: () -> Unit={}
            private set
        var metaData: Map<kotlin.String,Any> = emptyMap()
        private set


        init {
            this.publicMerchantKey = publicMerchantKey
        }
        fun dev(value:Boolean): Builder {
            dev = value
            return this
        }

        fun onSuccess(onSuccess:(String) -> Unit): Builder {
            this.onSuccess = onSuccess
            return this
        }
        fun onCancel(onCancel:() -> Unit): Builder {
            this.onCancel = onCancel
            return this
        }

        fun onFailed(onFailed:() -> Unit): Builder {
            this.onFailed = onFailed
            return this
        }

        fun onClose(onClose:() -> Unit): Builder {
            this.onClose = onClose
            return this
        }

        fun onRetry(onRetry:() -> Unit): Builder {
            this.onRetry = onRetry
            return this
        }

        fun customization(value:Customization): Builder {
            customization = value
            return this
        }

        fun userInfo(value: UserInfo?): Builder {
            userInfo = value
            return this
        }

        fun metaData(value: Map<kotlin.String,Any>): Builder {
            metaData = value
            return this
        }

        fun build(): LivenessCheckModule {
            return LivenessCheckModule(this)
        }
    }

    /**
     * Starts the Liveness check service.
     * @throws InvalidCredentialsException if the "publicMerchantKey" specified in the option is invalid or if the wrong value was supplied for the "dev" argument.
     * @throws InvalidArgumentException if an invalid color string was supplied in the "appearance" configuration.
     * @param context the context object passed.
     */

     fun start(context: Context) {


        //initialize Activity observer
        livenessActivityObserver = LivenessActivityObserver(this)

        mContext=context

        context.startActivity(
            Intent(context, LivenessCheckActivity::class.java).apply {
                putExtra(USER_NAME,userInfo?.firstName)
            }
        )

        //validate the MerchantKey length and the Color String
        validateProperties()
    }



    internal fun sendLivenessUrl() {

        val baseUrl= if (dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
        val url="${baseUrl}/services/${publicMerchantKey}/liveness"

        mContext.startActivity(
            Intent(mContext, LivenessCheckActivity::class.java).apply {
                putExtra(URL_TO_DISPLAY,url)
            }
        )
    }

    private fun validateProperties() {

        validatePublicMerchantKeyAndAppearance(
            publicMerchantKey = publicMerchantKey,
            appearance = customization
        )
    }

    fun close(){
        mContext.startActivity(
            Intent(mContext, LivenessCheckActivity::class.java).apply {
                putExtra(FINISH_ACTIVITY,true)
            }
        )

    }

}