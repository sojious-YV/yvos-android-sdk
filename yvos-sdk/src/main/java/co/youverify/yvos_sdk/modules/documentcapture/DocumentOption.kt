package co.youverify.yvos_sdk.modules.documentcapture

import co.youverify.yvos_sdk.Option

 class DocumentOption(

    publicMerchantKey:String,
    dev: Boolean=false,
    metadata: Map<String, Any> = emptyMap(),
    val personalInfo:DocumentPersonalInfo?=null,
    val countries: List<Country>?=null,
    val onSuccess: (DocumentData?) -> Unit,
    val onCancel: (DocumentData?) -> Unit,
    val onClose: (DocumentData?) -> Unit,

    ): Option(publicMerchantKey,dev,metadata)

 data class Country(
    val countryCode: String="",
    val idTypes: List<String> = emptyList(),
    val province: List<String> = emptyList()
)

data class DocumentPersonalInfo (val firstName:String="")


enum class DocumentType(val documentName: String) {
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
}


