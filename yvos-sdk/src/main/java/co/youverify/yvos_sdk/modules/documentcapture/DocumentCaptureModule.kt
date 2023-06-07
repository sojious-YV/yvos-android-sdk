package co.youverify.yvos_sdk.modules.documentcapture

import android.content.Context
import android.content.Intent
import android.graphics.Color
import co.youverify.yvos_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.yvos_sdk.util.ID_LENGTH
import co.youverify.yvos_sdk.util.PRODUCTION_BASE_URL
import co.youverify.yvos_sdk.util.SdkException
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.validatePublicMerchantKeyAndAppearance
import java.lang.IllegalArgumentException
import java.nio.charset.Charset
import kotlin.io.encoding.Base64

class DocumentCaptureModule(private val option: DocumentOption) {

    private lateinit var mContext: Context

    companion object{
        internal lateinit var documentActivityObserver:DocumentActivityObserver
    }

    /**
     * Displays the  vForm associated with this vFormModule
     * Throw SdkException if the provided form Id or public merchant key is invalid or if an attempt is made to display
     * a form created in production environment while setting "dev" option to true and vice-versa
     */
    @Throws(Exception::class)
    fun start(context: Context) {

        //initialize Activity observer
        documentActivityObserver= DocumentActivityObserver(this)
        documentActivityObserver.option=option


        context.startActivity(
            Intent(context, DocumentCaptureActivity::class.java).apply {
                putExtra(USER_NAME,option.personalInfo?.firstName)
            }
        )

        mContext=context

        //validate formId and MerchantKey length
        validateOption()

    }



    internal fun sendDocumentCaptureUrl() {

        val baseUrl=if (option.dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
        val primaryColorByteArray=option.appearance.primaryColor.toByteArray(Charsets.US_ASCII)
        val primaryColorBase64String=android.util.Base64.encodeToString(primaryColorByteArray,android.util.Base64.DEFAULT)
        val countries=""
        val url="${baseUrl}/services/${option.publicMerchantKey}/document?countries=${countries}&primaryColor=$primaryColorBase64String"


        mContext.startActivity(
            Intent(mContext, DocumentCaptureActivity::class.java).apply {
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