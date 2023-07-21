package co.youverify.yvos_sdk.modules.livenesscheck

import android.content.Context
import android.content.Intent
import co.youverify.yvos_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.yvos_sdk.exceptions.InvalidArgumentException
import co.youverify.yvos_sdk.exceptions.InvalidCredentialsException
import co.youverify.yvos_sdk.util.PRODUCTION_BASE_URL
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.validatePublicMerchantKeyAndAppearance
/**
* This class enables access to the Livesness check service.
* @property option holds the appearance specifications and information needed by the Liveness check service.
* @constructor creates an instant of this module whose configuration is specified by the "option" property.
*/
class LivenessCheckModule  constructor(private val option: LivenessOption) {


    private lateinit var mContext: Context

    companion object{
         internal lateinit var livenessActivityObserver: LivenessActivityObserver
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
        livenessActivityObserver.option=option
        mContext=context

        context.startActivity(
            Intent(context, LivenessCheckActivity::class.java).apply {
                putExtra(USER_NAME,option.personalInfo?.firstName)
            }
        )

        //validate the MerchantKey length and the Color String
        validateOption()
    }



    internal fun sendLivenessUrl() {

        val baseUrl= if (option.dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
        val url="${baseUrl}/services/${option.publicMerchantKey}/liveness"

        mContext.startActivity(
            Intent(mContext, LivenessCheckActivity::class.java).apply {
                putExtra(URL_TO_DISPLAY,url)
            }
        )
    }

    private fun validateOption() {

        validatePublicMerchantKeyAndAppearance(
            publicMerchantKey = option.publicMerchantKey,
            appearance = option.appearance
        )
    }

}