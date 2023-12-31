package br.com.richardnatan.radioanimu.model

import com.google.gson.annotations.SerializedName

data class AnimuApiResponse(
    @SerializedName("track") val music: Music,
    @SerializedName("listeners") val listeners: Int
)
