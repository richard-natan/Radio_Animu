package br.com.richardnatan.radioanimu.data

import br.com.richardnatan.radioanimu.model.UnixTime
import retrofit2.Call
import retrofit2.http.GET

interface UnixAPI {

    @GET("/api/timezone/America/Sao_Paulo/")
    fun findUnix(): Call<UnixTime>
}