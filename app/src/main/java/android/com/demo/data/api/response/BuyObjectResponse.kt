package android.com.demo.data.api.response

import com.google.gson.annotations.SerializedName

data class BuyObjectResponse(
    @SerializedName("id"       ) var id       : Int?    = null,
    @SerializedName("name"     ) var name     : String? = null,
    @SerializedName("price"    ) var price    : Int?    = null,
    @SerializedName("quantity" ) var quantity : Int?    = null,
    @SerializedName("type"     ) var type     : Int?    = null
)
