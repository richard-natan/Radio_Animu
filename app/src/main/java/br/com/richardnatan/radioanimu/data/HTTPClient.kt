package br.com.richardnatan.radioanimu.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HTTPClient {

    private const val ANIMU_BASE_URL = "https://api.animu.com.br/"
    private const val UNIX_BASE_URL = "https://worldtimeapi.org/api/timezone/America/Sao_Paulo/"

    fun retrofitAnimu(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ANIMU_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun retrofitUnixTime(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UNIX_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}