package br.com.richardnatan.radioanimu.data

import android.os.Handler
import android.os.Looper
import br.com.richardnatan.radioanimu.model.AnimuApiResponse
import br.com.richardnatan.radioanimu.model.Music
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MusicDataSource {

    fun getCurrentMusic(callback: MusicCallback) {
        Handler(Looper.getMainLooper()).post {
            HTTPClient.retrofitAnimu().create(AnimuAPI::class.java)
                .findMusic()
                .enqueue(object : Callback<AnimuApiResponse> {
                    override fun onResponse(
                        call: Call<AnimuApiResponse>,
                        response: Response<AnimuApiResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            callback.onSuccess(
                                responseBody ?: AnimuApiResponse(
                                    Music(
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                    ),
                                    0
                                )
                            )
                        } else {
                            val error = response.errorBody()?.toString()
                            callback.onError(error ?: "ERRO DESCONHECIDO")
                        }
                        callback.onComplete()
                    }

                    override fun onFailure(call: Call<AnimuApiResponse>, t: Throwable) {
                        callback.onError(t.message ?: "ERRO INTERNO")
                        callback.onComplete()
                    }

                })
        }

    }
}