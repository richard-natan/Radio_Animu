package br.com.richardnatan.radioanimu.model

import com.google.gson.annotations.SerializedName

data class UnixTime(
    @SerializedName("unixtime") val unixtime: Long
)
