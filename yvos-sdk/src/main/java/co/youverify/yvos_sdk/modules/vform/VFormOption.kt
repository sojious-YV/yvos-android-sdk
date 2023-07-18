package co.youverify.yvos_sdk.modules.vform

import co.youverify.yvos_sdk.Appearance
import co.youverify.yvos_sdk.Option

class VFormOption(
    val vFormId: String,
    publicMerchantKey:String,
    val personalInfo:VFormPersonalInfo?=null,
    val sandBoxEnvironment:Boolean=false,
    val appearance: Appearance=Appearance(
        greeting = "We will need to verify your identity. It will only take a moment.",
        actionText = "Verify Identity",
        buttonBackgroundColor = "#46B2C8",
        buttonTextColor = "#ffffff",
        primaryColor = "#46B2C8"
    ),
    val onSuccess: (VFormEntryData?) -> Unit = {},
    val onFailed: (VFormEntryData?) -> Unit={},
    val onCompleted: (VFormEntryData?) -> Unit={},
    dev: Boolean=false,
    metadata: Map<String, Any> = emptyMap(),


    ): Option(publicMerchantKey,dev,metadata)


data class VFormPersonalInfo(
    val firstName:String?=null,
    val lastName:String?=null,
    val middleName:String?=null,
    val email:String?=null,
    val mobile:String?=null,
    val gender:GenderType=GenderType.NOT_IDENTIFIED
)



enum class GenderType{
    MALE,
    FEMALE,
    NOT_IDENTIFIED
}