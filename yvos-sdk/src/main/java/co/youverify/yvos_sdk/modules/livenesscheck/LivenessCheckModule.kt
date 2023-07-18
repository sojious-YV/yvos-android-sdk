package co.youverify.yvos_sdk.modules.livenesscheck

import android.content.Context
import android.content.Intent
import co.youverify.yvos_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.yvos_sdk.util.PRODUCTION_BASE_URL
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.validatePublicMerchantKeyAndAppearance

class LivenessCheckModule  constructor(private val option: LivenessOption) {


    private lateinit var mContext: Context

    companion object{
         internal lateinit var livenessActivityObserver: LivenessActivityObserver
     }

    /**
     * Displays the  vForm associated with this vFormModule
     * Throw SdkException if the provided form Id or public merchant key is invalid or if an attempt is made to display
     * a form created in production environment while setting "dev" option to true and vice-versa
     */
    @Throws(Exception::class)
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

        //validate formId and MerchantKey length
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