package co.youverify.workflow_builder_sdk.modules.vform

import co.youverify.workflow_builder_sdk.Option

class VFormOption(
    val vFormId: String,
    publicMerchantKey:String,
    personalInfo:VFormPersonalInfo?=null,
    val onSuccess: (VFormResultData) -> Unit = {},
    val onClose: () -> Unit={},
    dev: Boolean=false,
    metadata: Map<String, Any> = emptyMap(),


    ): Option(publicMerchantKey,dev,metadata)


data class VFormPersonalInfo(
    val firstName:String="",
    val lastName:String="",
    val middleName:String="",
    val email:String="",
    val mobile:String="",
    val gender:String=""
)

data class VFormResultData(
    val data: Data
)

data class Data(
    val entry: Entry
)

data class Entry(
    val id: String,
    val fields: List<Field>
)

data class Field(
    val value: Boolean?,
    val middleName: String?,
    val email: String?,
    val dateOfBirth: String?,
    val gender: String?,
    val mobile: String?,
    val photo: String?,
    val relationship: String?,
    val _id: String,
    val validated: Boolean,
    val __type: String,
    val label: String,
    val fieldTemplateId: String,
    val firstName: String?,
    val lastName: String?,
    val id: String
)
enum class GenderType{
    MALE,
    FEMALE,
    NOT_IDENTIFIED
}