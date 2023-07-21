package co.youverify.yvos_sdk

/**
 * The base class that specifies the common information needed by all modules. Individual modules have their
 * specific options which extend from this base class.
 * @property publicMerchantKey the user business Id which can be gotten from their YVOS dashboard.
 * @property dev specifies whether the user is in development mode or not.
 * @property metadata information you would like to pass to your webhook URL.
 * @constructor create an option with properties passed into the constructor.
 */
abstract class Option(
    val publicMerchantKey: String,
    val dev: Boolean,
    val metadata: Map<String, Any>
    )



