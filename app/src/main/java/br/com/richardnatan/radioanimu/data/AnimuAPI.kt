package br.com.richardnatan.radioanimu.data

import br.com.richardnatan.radioanimu.model.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface AnimuAPI {

    @GET("/")
    fun findMusic(): Call<ApiResponse>
}