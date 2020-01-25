package com.mindorks.bootcamp.instagram.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class PostCreationResponse(

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
    var data: PostData
){
    data class PostData(

        @Expose
        @SerializedName("id")
        val id : String,

        @Expose
        @SerializedName("imgUrl")
        val imageUrl : String,

        @Expose
        @SerializedName("imgWidth")
        val imageWidth : Int,

        @Expose
        @SerializedName("imgHeight")
        val imageHeight : Int,

        @Expose
        @SerializedName("createdAt")
        val createdAt : Date

    )
}