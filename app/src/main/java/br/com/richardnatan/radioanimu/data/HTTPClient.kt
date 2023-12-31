package br.com.richardnatan.radioanimu.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HTTPClient {

    private const val ANIMU_BASE_URL = "https://api.animu.com.br/"
    private const val ANIMU_DJ_URL = "https://www.animu.com.br/"
    private const val UNIX_BASE_URL = "https://worldtimeapi.org/api/timezone/America/Sao_Paulo/"


    private fun httpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun retrofitAnimu(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ANIMU_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun retrofitAnimuDj(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ANIMU_DJ_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient())
            .build()
    }

    fun retrofitUnixTime(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(UNIX_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}