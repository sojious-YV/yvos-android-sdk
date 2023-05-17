package co.youverify.workflow_builder_sdk.modules.documentcapture

import android.content.Context
import android.content.Intent
import co.youverify.workflow_builder_sdk.util.DEVELOPMENT_BASE_URL
import co.youverify.workflow_builder_sdk.util.ID_LENGTH
import co.youverify.workflow_builder_sdk.util.PRODUCTION_BASE_URL
import co.youverify.workflow_builder_sdk.util.SdkException
import co.youverify.workflow_builder_sdk.util.URL_TO_DISPLAY

class DocumentCaptureModule(val option: DocumentOption) {

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

        //validate formId and MerchantKey length
        validateOption()
        loadDocumentCaptureUrl(context)
    }



    private fun loadDocumentCaptureUrl(context: Context) {

        val baseUrl=if (option.dev) DEVELOPMENT_BASE_URL else PRODUCTION_BASE_URL
        val countries=""
        val url="${baseUrl}/services/${option.publicMerchantKey}/document?countries=${countries}&primaryColor=#46B2C8"

        documentActivityObserver= DocumentActivityObserver()
        documentActivityObserver.option=option
        documentActivityObserver.onSuccessCallback=option.onSuccess
        documentActivityObserver.onCloseCallback=option.onClose
        documentActivityObserver.onCancelCallback=option.onCancel

        context.startActivity(
            Intent(context, DocumentCaptureActivity::class.java).apply {
                putExtra(URL_TO_DISPLAY,url)
            }
        )
    }

    private fun validateOption() {
        if (option.publicMerchantKey.length!= ID_LENGTH || option.publicMerchantKey.isEmpty())
            throw SdkException("public merchant key cannot be empty and must be 24 characters long")
        if (option.countries!=null){
            for (country in option.countries){
                if (country.countryCode.isNotEmpty()&&country.idTypes.isNotEmpty()){
                    val idTypes=IdType.values()
                    val idNames=List(4){ idTypes[it].name }
                    for (idType in country.idTypes){
                        if (!idNames.contains(idType))
                            throw SdkException("public merchant key cannot be empty and must be 24 characters long")
                    }
                }

            }
        }

    }
}