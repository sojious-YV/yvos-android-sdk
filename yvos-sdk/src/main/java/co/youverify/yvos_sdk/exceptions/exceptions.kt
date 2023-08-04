package co.youverify.yvos_sdk.exceptions

/**
 * The generic exception class thrown when an occurred is encountered while using the SDK
 * @param errorMessage is the message that describe the exception
 */
open class SdkException(errorMessage: String): Exception(errorMessage)

/**
 * The exception that indicates the the user supplied wrong credentials while initializing the SDK
 * @param errorMessage  the message that describe the exception
 */
class InvalidCredentialsException(errorMessage: String):SdkException(errorMessage)

/**
 * The exception that indicates the the user supplied an invalid argument to some method or constructor of the SDK classes
 * @param errorMessage  the message that describe the exception
 */
//class InvalidArgumentException(errorMessage: String):SdkException(errorMessage)