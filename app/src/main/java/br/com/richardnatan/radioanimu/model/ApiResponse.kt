package br.com.richardnatan.radioanimu.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("track") val music: Music
)
