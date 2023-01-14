package android.com.demo.data.api

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.logging.HttpLoggingInterceptor

class PrettyLogger : HttpLoggingInterceptor.Logger {
    private val mGson = GsonBuilder().setPrettyPrinting().create()
    private val mJsonParser = JsonParser()
    private val TAG_API = "GSON"
    override fun log(message: String) {
        val trimMessage = message.trim { it <= ' ' }
        if (trimMessage.startsWith("{") && trimMessage.endsWith("}")
            || trimMessage.startsWith("[") && trimMessage.endsWith("]")
        ) {
            try {
                val prettyJson = mGson.toJson(mJsonParser.parse(message))
                Log.i(TAG_API, prettyJson)
            } catch (e: Exception) {
                Log.e(TAG_API, message, e)
            }
        } else {
            Log.i(TAG_API, message)
        }
    }
}