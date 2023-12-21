package br.com.richardnatan.radioanimu.model

import com.google.gson.annotations.SerializedName

data class Music(
    @SerializedName("title") val name: String?,
    @SerializedName("artist") val author: String?,
    @SerializedName("album") val imageUrl: String?,
    @SerializedName("timestart") val timeStart: Long?,
    @SerializedName("duration") val duration: Long?,
)