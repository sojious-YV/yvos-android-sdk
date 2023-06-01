package co.youverify.yvos_sdk.modules.livenesscheck

import android.content.Context
import android.content.Intent
import co.youverify.yvos_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.yvos_sdk.util.ID_LENGTH
import co.youverify.yvos_sdk.util.PRODUCTION_BASE_URL
import co.youverify.yvos_sdk.util.SdkException
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME

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


        context.startActivity(
            Intent(context, LivenessCheckActivity::class.java).apply {
                putExtra(USER_NAME,option.personalInfo?.firstName)
            }
        )

        mContext=context

        //validate formId and MerchantKey length
        validateOption()
        //sendLivenessUrl()
    }



    internal fun sendLivenessUrl() {

        val baseUrl=if (option.dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
        val url="${baseUrl}/services/${option.publicMerchantKey}/liveness"

        mContext.startActivity(
            Intent(mContext, LivenessCheckActivity::class.java).apply {
                putExtra(URL_TO_DISPLAY,url)
            }
        )
    }

    private fun validateOption() {
        if (option.publicMerchantKey.length!= ID_LENGTH || option.publicMerchantKey.isEmpty())
            throw SdkException("public merchant key cannot be empty and must be 24 characters long")

    }

}