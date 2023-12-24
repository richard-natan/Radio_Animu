package br.com.richardnatan.radioanimu.model

import com.google.gson.annotations.SerializedName

data class Artworks(
    @SerializedName("tiny") val tiny: String,
    @SerializedName("medium") val medium: String,
    @SerializedName("large") val large: String
)
