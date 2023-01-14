package android.com.demo.di

import android.com.demo.data.api.ApiService
import android.com.demo.data.api.PrettyLogger
import android.com.demo.data.api.responeFactory.CustomCallAdapterFactory
import android.com.demo.utils.Constants
import android.com.demo.utils.Constants.API_KEY
import android.com.demo.utils.Constants.API_KEY_VALUE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor{
        val prettyLogInterceptor = HttpLoggingInterceptor(PrettyLogger())
        prettyLogInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return  prettyLogInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(prettyLogInterceptor: HttpLoggingInterceptor): OkHttpClient{

        val builderOkHttpClient = OkHttpClient.Builder()
        //add interceptor for pretty log
        builderOkHttpClient.addInterceptor(prettyLogInterceptor)
        builderOkHttpClient.networkInterceptors().add(Interceptor { chain ->
            val build = chain.request().newBuilder().addHeader(
                API_KEY,
                API_KEY_VALUE
            ).build()
            chain.proceed(build)
        })

        return builderOkHttpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CustomCallAdapterFactory())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
