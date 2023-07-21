package co.youverify.yvos_sdk.modules.vform

import co.youverify.yvos_sdk.Appearance
import co.youverify.yvos_sdk.Option

/**
 * Specifies both required and optional information needed to display the appropriate form.
 * @property dev specifies whether the client is in development environment, defaults to false.
 *@property vFormId the template id of the form to be shown
 * @param publicMerchantKey the client's business id.
 *@property personalInfo user information which may or may not already exist as fields in the form. If any of the user information defined exist in the
 * the form to be shown, it is automatically filled and excluded from the form fields when the form is eventually shown,
 * otherwise the field is ignored.
 * @property sandBoxEnvironment specifies wether the client is developing in a sandbox Environment, defaults to false.
 * @property appearance allows the customization of the UI of the sdk to suit your app's theme.
 * @property onSuccess callback to notify when the form has been submitted successfully. The returned String is a JSON string - a key-value pairs of field names and their filled in values.
 * It is guaranteed that when this callback is triggered, the form in question has been submitted successfully, hence you can allow the users of your app proceed with the next step in your app's flow.
 * @property onFailed callback to notify when form submission fails.
 * @property onCompleted callback to notify when the form submission process completes succesfully. The notable difference from [onSuccess] is that is is triggered immediately after it.
 * @constructor creates a VFormOption with the specified properties.
 */
class VFormOption(
    val vFormId: String,
    publicMerchantKey: String,
    val personalInfo:VFormPersonalInfo?=null,
    val sandBoxEnvironment:Boolean=false,
    val appearance: Appearance=Appearance(
        greeting = "We will need to verify your identity. It will only take a moment.",
        actionText = "Verify Identity",
        buttonBackgroundColor = "#46B2C8",
        buttonTextColor = "#ffffff",
        primaryColor = "#46B2C8"
    ),
    val onSuccess: (String) -> Unit = {},
    val onFailed: () -> Unit={},
    val onCompleted: (String) -> Unit={},
    dev: Boolean=false,
    metadata: Map<kotlin.String, Any> = emptyMap(),


    ): Option(publicMerchantKey,dev,metadata)

/**
 * This class holds user information for the Vform service.
 * Note that if any of the properties exits in the form to be displayed,it will automatically be populated and therefore excluded from
 * the form fields when the form is eventually displayed to the user
 * @property firstName user's first name.If this is not passed, no customized greeting message is shown to the user before
 * the form is displayed.
 * @property lastName user's last name.
 * @property middleName user's middle name.
 * @property email user's email.
 * @property mobile user's phone number.
 * @property gender user's gender.
 * @constructor creates a VFormPersonalInfo instance which holds the user's information.
 */
data class VFormPersonalInfo(
    val firstName: String?=null,
    val lastName: String?=null,
    val middleName: String?=null,
    val email: String?=null,
    val mobile: String?=null,
    val gender:GenderType=GenderType.NOT_IDENTIFIED
)


/**
 * Enumeration of valid user gender types can be passed when creating a "VFormPersonalInfo" instance.
 */
 enum class GenderType{
    MALE,
    FEMALE,
    NOT_IDENTIFIED
}