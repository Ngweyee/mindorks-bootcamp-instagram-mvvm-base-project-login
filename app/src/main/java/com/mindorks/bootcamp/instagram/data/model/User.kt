package com.mindorks.bootcamp.instagram.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(

    @Expose
    @SerializedName("userId")
    val id: String = "",

    @Expose
    @SerializedName("name")
    val name: String = "",

    @Expose
    @SerializedName("email")
    val email: String = "",

    @Expose
    @SerializedName("accessToken")
    val accessToken: String = "",

    @Expose
    @SerializedName("profilePicUrl")
    val profilePicUrl: String? = null,

    @Expose
    @SerializedName("tagline")
    val tagline: String? = null
) : Parcelable