package co.youverify.yvos_sdk.util


import android.graphics.Color
import co.youverify.yvos_sdk.Appearance
import retrofit2.Response
import java.io.IOException
import java.lang.IllegalArgumentException


internal suspend fun<T:Any> handleApi(callApi: suspend () -> Response<T>): NetworkResult<T> {


    return try{
        val response=callApi()
        val body=response.body()

        //for a response with a body
        if (response.isSuccessful && body!=null)
            NetworkResult.Success(data = body)
        else
        //for a response with error message
            NetworkResult.Error(code = response.code(), message = response.message())

        //for exceptions
    }catch (exception: IOException){
        //throw IOException(exception.message)
        NetworkResult.Exception(e=exception, genericMessage = "Ooops, Could not connect to the server... check your internet connection and try again")
    }catch (exception:Exception){
        //throw exception
        NetworkResult.Exception(e=exception, genericMessage = "Ooops, Something went wrong..try again")

    }

}

fun validatePublicMerchantKeyAndAppearance(publicMerchantKey:String,appearance: Appearance){

    if (publicMerchantKey.length!= ID_LENGTH ||publicMerchantKey.isEmpty())
        throw SdkException("public merchant key cannot be empty and must be 24 characters long")

    try{
        Color.parseColor(appearance.primaryColor)
    }catch (exception: IllegalArgumentException){
        throw SdkException("The primary color string is invalid, it should be an hex code such as: \"#ffffff\" ")
    }

    try{
        Color.parseColor(appearance.buttonTextColor)
    }catch (exception: IllegalArgumentException){
        throw SdkException("The button text color string is invalid, it should be an hex code such as: \"#ffffff\" ")
    }

    try{
        Color.parseColor(appearance.buttonBackgroundColor)
    }catch (exception: IllegalArgumentException){
        throw SdkException("The button background color string is invalid, it should be an hex code such as: \"#ffffff\" ")
    }
}



internal sealed class NetworkResult<T:Any>{


    class Success<T : Any>(val data: T):NetworkResult<T>()
    class Error<T : Any>(val code: Int, val message: String?):NetworkResult<T>()
    class Exception<T:Any>(val e:Throwable, val genericMessage:String):NetworkResult<T>()


}


internal class SdkException( errorMessage: String): Exception(errorMessage)