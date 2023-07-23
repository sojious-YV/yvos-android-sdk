package co.youverify.yvos_sdk.data

import co.youverify.yvos_sdk.SdkModule
import co.youverify.yvos_sdk.modules.documentcapture.DocumentCaptureModule
import co.youverify.yvos_sdk.modules.livenesscheck.LivenessCheckModule
import co.youverify.yvos_sdk.modules.vform.VFormModule
import co.youverify.yvos_sdk.util.VFORM_DEVELOPMENT_API_BASE_URL
import co.youverify.yvos_sdk.util.IDENTITY_DEVELOPMENT_API_BASE_URL
import co.youverify.yvos_sdk.util.VFORM_PRODUCTION_API_BASE_URL
import co.youverify.yvos_sdk.util.VFORM_STAGING_API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal object SdkServiceFactory {

     private val service:SdkService?=null

    fun sdkService(module: SdkModule):SdkService{

       val baseUrl= when(module){
            is VFormModule->{
                if (module.dev && module.sandBoxEnvironment) VFORM_STAGING_API_BASE_URL
                else if (!module.dev) VFORM_PRODUCTION_API_BASE_URL
                else  VFORM_DEVELOPMENT_API_BASE_URL

            }
            is LivenessCheckModule ->{
                IDENTITY_DEVELOPMENT_API_BASE_URL
            }
            is DocumentCaptureModule->{
                IDENTITY_DEVELOPMENT_API_BASE_URL
            }
           else -> {""}
       }

        return service
            ?: Retrofit.Builder()
                .client(httpClient(loggingInterceptor()))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(SdkService::class.java)

    }

     private fun loggingInterceptor(): HttpLoggingInterceptor {

         val logging= HttpLoggingInterceptor()
         logging.level=  HttpLoggingInterceptor.Level.BODY

         return logging
     }

    private fun httpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            //.addInterceptor(tokenInterceptor)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
}