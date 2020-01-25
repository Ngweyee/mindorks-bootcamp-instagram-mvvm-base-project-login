package com.mindorks.bootcamp.instagram.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageResponse(

    @Expose
    @SerializedName("statusCode")
    var statusCode: String,

    @Expose
    @SerializedName("status")
    var status: String,

    @Expose
    @SerializedName("message")
    var message: String,

    @Expose
    @SerializedName("data")
    var data: ImageData
) {
    data class ImageData(

        @Expose
        @SerializedName("imageUrl")
        var imageUrl: String
    )
}