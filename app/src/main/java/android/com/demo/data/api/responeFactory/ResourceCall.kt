package android.com.demo.data.api.responeFactory

import android.com.demo.data.api.Resource
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class ResourceCall<T>(proxy: Call<T>) : CallDelegate<T, Resource<T>>(proxy){
    override fun enqueueImpl(callback: Callback<Resource<T>>) = proxy.enqueue(object: Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val result = if (code in 200 until 300) {
                val body = response.body()
                val successResult: Resource<T> = Resource.success(body)
                successResult
            } else {
                Resource.error("Response code: $code", null)
            }

            callback.onResponse(this@ResourceCall, Response.success(result))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result = Resource.error(t.message ?: "Can not get error messave", null)
            callback.onResponse(this@ResourceCall, Response.success(result))
        }
    })

    override fun cloneImpl() = ResourceCall(proxy.clone())
    override fun timeout(): Timeout {
        return Timeout().timeout(30,TimeUnit.SECONDS)
    }
}