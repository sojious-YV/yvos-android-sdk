package co.youverify.yvos_sdk.modules.documentcapture

/**
 * Specifies both required and optional information needed to start the liveness check.
 * @property dev specifies whether the client is in development environment, defaults to false.
 * @param publicMerchantKey the client's business id.
 * @property personalInfo user information.
 * @property sandBoxEnvironment specifies whether the client is developing in a sandbox Environment, defaults to false.
 * @property appearance allows the customization of the UI of the sdk to suit your app's theme.
 * @property onSuccess callback to notify when the document has been captured successfully. The returned DocumentData object contains the document information.
 * @property onFailure callback to notify when the document capture fails.
 * @property onClose callback to notify when the document capture process is closed. it is triggered when the "close" button is clicked and essentially restarts the Liveness check.
 * @property onCancel callback to notify when the document capture process is cancelled. It is triggered when the "x" modal button is clicked.It is triggered when the "x" modal button is clicked or immediately after [onClose] when the "close" button is clicked.It has the same effect as [onClose].
 * @property onRetry callback to notify when the document capture process is being retried after having previously failed. it is triggered when the "retry" button is clicked.
 * @property countries the list of countries which will be available for users to select from.
 * @constructor creates a DocumentOption with the specified properties.
 */
 /*class DocumentOption(

    publicMerchantKey:String,
    dev: Boolean=false,
    val sandBoxEnvironment:Boolean=false,
    metadata: Map<String, Any> = emptyMap(),
    val personalInfo:DocumentPersonalInfo?=null,
    //val appearance: Appearance = Appearance(
       greeting = "We will need to carry out a  document capture. It will only take a moment.",
       actionText = "Start Document Capture",
       buttonBackgroundColor = "#46B2C8",
       buttonTextColor = "#ffffff",
       primaryColor = "#46B2C8"
    ),
    val countries: List<Country>?=null,
    val onSuccess: (DocumentData) -> Unit,
    val onCancel: () -> Unit,
    val onClose: () -> Unit,

    ): Option(publicMerchantKey,dev,metadata)*/

/**
 * Holds information about the country to be used for document capture.
 * For a list of country codes, provinces and supported id types see (<https://www.google.com>)
 * @property countryCode a list of standard country codes e.g "US', "NG"
 * @property idTypes a list of supported id types in the country.
 * @property province a list of provinces in the country.
 */
 /*data class Country(
    val countryCode: String="",
    val idTypes: List<String> = emptyList(),
    val province: List<String> = emptyList()
)*/

/**
 * This class holds user information for the document capture service.
 * @property firstName user's first name.If this is not passed, no customized greeting message is shown to the user before
 * the document capture process begins.
 * @constructor creates a DocumentPersonalInfo instance which holds the user's first name.
 */
/*data class DocumentPersonalInfo (val firstName:String="")


internal enum class DocumentType(val documentName: String) {
   NATIONAL_ID("national_id"),
   VOTERS_CARD("voters_card"),
   DRIVERS_LICENSE("drivers_license"),
   DRIVERS_CARD("drivers_card"),
   PASSPORT("passport"),
   ID_CARD("id_card"),
   PROFESSIONAL_DL("professional_dl"),
   ALIEN_ID("alien_id"),
   PROOF_OF_AGE_CARD("proof_of_age_card"),
   ID_CARDB("id_cardb"),
   MINORS_CARD("minors_card"),
   RESIDENCE_PERMIT("residence_permit"),
   TEMPORARY_RESIDENCE_PERMIT("temporary_residence_permit"),
   CITIZENSHIP_CERTIFICATE("citizenship_certificate"),
   TRIBAL_ID("tribal_id"),
   WEAPON_PERMIT("weapon_permit"),
   PUBLIC_SERVICES_CARD("public_services_card"),
   CONSULAR_ID("consular_id"),
   PAN_CARD("pan_card"),
   TAX_ID("tax_id"),
   MILITARY_ID("military_id"),
   MY_KAS("my_kas"),
   MY_KAD("my_kad"),
   MY_KID("my_kid"),
   MY_PR("my_pr"),
   MY_POLIS("my_polis"),
   REFUGEE_ID("refugee_id"),
   I_KAD("i-kad"),
   FIN_CARD("fin_card"),
   WORK_PERMIT("work_permit"),
   SOCIAL_SECURITY_CARD("social_security_card"),
   GREEN_CARD("green_card"),
   NEXUS_CARD("nexus_card")
}*/


