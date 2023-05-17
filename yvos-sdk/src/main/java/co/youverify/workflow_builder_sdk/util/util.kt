package co.youverify.workflow_builder_sdk.util


import retrofit2.Response
import java.io.IOException


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
        throw IOException(exception.message)
    }catch (exception:Exception){
        throw exception
    }

}





internal sealed class NetworkResult<T:Any>{


    class Success<T : Any>(val data: T):NetworkResult<T>()
    class Error<T : Any>(val code: Int, val message: String?):NetworkResult<T>()
    class Exception<T:Any>(val e:Throwable, val genericMessage:String):NetworkResult<T>()


}


internal class SdkException( errorMessage: String): Exception(errorMessage)