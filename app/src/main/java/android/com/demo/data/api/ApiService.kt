package android.com.demo.data.api

import android.com.demo.data.api.response.BuyObjectResponse
import android.com.demo.data.api.response.CallObjectResponse
import retrofit2.http.GET

interface ApiService {
    @GET("/imkhan334/demo-1/call")
    suspend fun getCallList(): Resource<List<CallObjectResponse>>

    @GET("/imkhan334/demo-1/buy")
    suspend fun getBuyList(): Resource<List<BuyObjectResponse>>
}