package android.com.demo.data.api.response

import com.google.gson.annotations.SerializedName

data class CallObjectResponse(
    @SerializedName("id"     ) var id     : Int?    = null,
    @SerializedName("name"   ) var name   : String? = null,
    @SerializedName("number" ) var number : String? = null
)
