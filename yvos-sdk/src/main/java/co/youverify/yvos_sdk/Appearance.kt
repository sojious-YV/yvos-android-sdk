package co.youverify.yvos_sdk

import co.youverify.yvos_sdk.exceptions.InvalidArgumentException

/**
 * This class specifies the customization options of the SDK. The SDK uses the options supplied to customize the look of it's UI.
 * @property greeting the greeting message that is shown to the user before starting any of the SDK services.
 * Note that the greeting message only appears if you passed the user's first name while creating the user's personal information.
 * @property actionText the text of the action button beneath the greeting message.
 * @property buttonBackgroundColor the hex color code String that defines the background color of the action button. Passing an invalid color string will result in an [InvalidArgumentException]
 * @property buttonTextColor the hex color code String that defines the color of the action button text. Passing an invalid color string will result in an [InvalidArgumentException]
 * @property primaryColor the hex color code String that defines the primary color of the UI.This color is used to paint the progress indicator and some other UI elements. Passing an invalid color string will result in an [InvalidArgumentException]
 */

data class Appearance(
    val primaryColor: String,
    val greeting: String,
    val actionText: String,
    val buttonBackgroundColor: String,
    val buttonTextColor: String,

)