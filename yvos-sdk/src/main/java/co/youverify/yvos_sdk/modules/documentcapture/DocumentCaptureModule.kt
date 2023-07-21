
package co.youverify.yvos_sdk.modules.documentcapture
import android.content.Context
import android.content.Intent
import co.youverify.yvos_sdk.exceptions.InvalidArgumentException
import co.youverify.yvos_sdk.exceptions.InvalidCredentialsException
import co.youverify.yvos_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.yvos_sdk.util.PRODUCTION_BASE_URL
import co.youverify.yvos_sdk.util.URL_TO_DISPLAY
import co.youverify.yvos_sdk.util.USER_NAME
import co.youverify.yvos_sdk.util.validatePublicMerchantKeyAndAppearance
import kotlin.jvm.Throws

/**
 * This class enables access to the Document capture service.
 * @property option holds the appearance specifications and information needed by the Document capture service.
 * @constructor creates an instant of this module whose configuration is specified by the "option" property.
 */
class DocumentCaptureModule(private val option: DocumentOption) {

    private lateinit var mContext: Context

    companion object{
        internal lateinit var documentActivityObserver:DocumentActivityObserver
    }

    /**
     * Starts the Document capture service.
     * @throws InvalidCredentialsException if the "publicMerchantKey" specified in the option is invalid or if the wrong value was supplied for the "dev" argument.
     * @throws InvalidArgumentException if an invalid color string was supplied in the "appearance" configuration.
     * @param context the context object passed.
     *
     */


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

        //validate the MerchantKey length and the Color String
        validateOption()

    }



    internal fun sendDocumentCaptureUrl() {

        val baseUrl= if (option.dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
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