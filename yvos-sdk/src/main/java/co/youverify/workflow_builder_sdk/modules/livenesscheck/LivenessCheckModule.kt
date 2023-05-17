package co.youverify.workflow_builder_sdk.modules.livenesscheck

import android.content.Context
import android.content.Intent
import co.youverify.workflow_builder_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.workflow_builder_sdk.util.ID_LENGTH
import co.youverify.workflow_builder_sdk.util.PRODUCTION_BASE_URL
import co.youverify.workflow_builder_sdk.util.SdkException
import co.youverify.workflow_builder_sdk.util.URL_TO_DISPLAY

class LivenessCheckModule  constructor(private val option: LivenessOption) {



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
        //validate formId and MerchantKey length
        validateOption()
        loadLivenessUrl(context)
    }



    private fun loadLivenessUrl(context: Context) {

        val baseUrl=if (option.dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
        val url="${baseUrl}/services/${option.publicMerchantKey}/liveness"

        livenessActivityObserver=LivenessActivityObserver()
        livenessActivityObserver.onSuccessCallback=option.onSuccess
        livenessActivityObserver.onCloseCallback=option.onClose
        livenessActivityObserver.onFailureCallback=option.onFailure
        livenessActivityObserver.onRetryCallback=option.onRetry
        livenessActivityObserver.option=option

        context.startActivity(
            Intent(context, LivenessCheckActivity::class.java).apply {
                putExtra(URL_TO_DISPLAY,url)
            }
        )
    }

    private fun validateOption() {
        if (option.publicMerchantKey.length!= ID_LENGTH || option.publicMerchantKey.isEmpty())
            throw SdkException("public merchant key cannot be empty and must be 24 characters long")

    }

}