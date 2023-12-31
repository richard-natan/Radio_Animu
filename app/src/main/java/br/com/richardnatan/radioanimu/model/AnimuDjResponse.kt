package br.com.richardnatan.radioanimu.model

import com.google.gson.annotations.SerializedName

data class AnimuDjResponse(
    @SerializedName("locutor") val announcer: String,
    @SerializedName("programa") val program: String,
    @SerializedName("pedidos_ao_vivo") val liveOrders: String,
    @SerializedName("imagem") val image: String,
    @SerializedName("pedidos_programa") val orderAutoDJ: String

)