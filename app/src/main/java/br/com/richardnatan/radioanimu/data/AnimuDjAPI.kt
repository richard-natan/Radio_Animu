package br.com.richardnatan.radioanimu.data

import br.com.richardnatan.radioanimu.model.AnimuDjResponse
import retrofit2.Call
import retrofit2.http.GET

interface AnimuDjAPI {

    @GET("/teste/locutor.php/")
    fun findDJ(): Call<AnimuDjResponse>
}