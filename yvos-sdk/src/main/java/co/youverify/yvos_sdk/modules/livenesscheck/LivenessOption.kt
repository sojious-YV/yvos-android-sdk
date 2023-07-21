package co.youverify.yvos_sdk.modules.livenesscheck

import co.youverify.yvos_sdk.Appearance
import co.youverify.yvos_sdk.Option

/**
 * Specifies both required and optional information needed to start the liveness check.
 * @property dev specifies whether the client is in development environment, defaults to false.
 * @param publicMerchantKey the client's business id.
 * @property personalInfo user information.
 * @property sandBoxEnvironment specifies whether the client is developing in a sandbox Environment, defaults to false.
 * @property appearance allows the customization of the UI of the sdk to suit your app's theme.
 * @property onSuccess callback to notify when the liveness check completes successfully. The returned LivenessData object contains the snapshot of the user taken during the process encoded as a base64 String.
 * @property onFailure callback to notify when the liveness check fails.
 * @property onClose callback to notify when the liveness is closed. it is triggered when the "close" button is clicked and essentially restarts the Liveness check.
 * @property onCancel callback to notify when the liveness check is cancelled. It is triggered when the "x" modal button is clicked or immediately after [onClose] when the "close" button is clicked.It has the same effect as [onClose].
 * @property onRetry callback to notify when the liveness check is being retried after having previously failed. it is triggered when the "retry" button is clicked .
 * @constructor creates a LivenessOption with the specified properties.
 */
class LivenessOption(
    publicMerchantKey:String,
    dev: Boolean=false,
    val sandBoxEnvironment:Boolean=false,
    metadata: Map<String, Any> = emptyMap(),
    val personalInfo: LivenessPersonalInfo?=null,
    val appearance: Appearance=Appearance(
        greeting = "We will need to carry out a liveness check. It will only take a moment.",
        actionText = "Start Liveness Test",
        buttonBackgroundColor = "#46B2C8",
        buttonTextColor = "#ffffff",
        primaryColor = "#46B2C8"
    ),
    val onSuccess: (LivenessData) -> Unit={},
    val onFailure: () -> Unit={},
    val onClose: () -> Unit={},
    val onCancel: () -> Unit={},
    val onRetry: () -> Unit={},
): Option(publicMerchantKey,dev,metadata)

/**
 * This class holds user information for the liveness check service.
 * @property firstName user's first name.If this is not passed, no customized greeting message is shown to the user before
 * the document capture process begins.
 * @property lastName user's last name.
 * @constructor creates a LivenessPersonalInfo instance which holds the user's first name and last name.
 */
data class LivenessPersonalInfo(val firstName:String="",val lastName:String="")




