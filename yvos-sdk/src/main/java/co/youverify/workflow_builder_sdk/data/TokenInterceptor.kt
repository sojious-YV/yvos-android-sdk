package co.youverify.workflow_builder_sdk.data

import okhttp3.Interceptor
import okhttp3.Response

internal class TokenInterceptor: Interceptor {



    private val token:String="0HGwwgWy.e4tuJmKuiyrT89ipubc8LO6qKCazxbPjc0cA"



    override fun intercept(chain: Interceptor.Chain): Response {

        var request=chain.request()


        request=request.newBuilder()
            .addHeader("Content-Type","application/json")
            .addHeader("Authorization","Bearer $token")
            .build()

        return chain.proceed(request)
    }
}
