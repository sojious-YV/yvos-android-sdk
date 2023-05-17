package co.youverify.workflow_builder_sdk.di

import co.youverify.workflow_builder_sdk.data.TokenInterceptor
import co.youverify.workflow_builder_sdk.data.SdkService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton



    @Module
  internal  object NetworkModule {



        @Provides
        @Singleton
        fun provideSdkService(okHttpClient: OkHttpClient): SdkService =
            Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SdkService::class.java)

        @Provides
        @Singleton
        fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                //.addInterceptor(tokenInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()


        @Provides
        @Singleton
        fun provideLoggingInterceptor(): HttpLoggingInterceptor {

            val logging= HttpLoggingInterceptor()
            logging.level=  HttpLoggingInterceptor.Level.BODY

            return logging
        }

        @Provides
        @Singleton
        fun provideTokenInterceptor(): TokenInterceptor = TokenInterceptor()



    }