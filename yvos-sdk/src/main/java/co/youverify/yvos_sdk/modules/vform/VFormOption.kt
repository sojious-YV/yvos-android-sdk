package co.youverify.yvos_sdk.modules.vform

import co.youverify.yvos_sdk.Option

class VFormOption(
    val vFormId: String,
    publicMerchantKey:String,
    val personalInfo:VFormPersonalInfo?=null,
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