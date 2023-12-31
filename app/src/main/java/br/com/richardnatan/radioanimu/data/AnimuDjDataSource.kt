package br.com.richardnatan.radioanimu.data

import android.os.Handler
import android.os.Looper
import android.util.Log
import br.com.richardnatan.radioanimu.model.AnimuDjResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimuDjDataSource {

    fun getCurrentDj(callback: AnimuDJCallback) {
        Handler(Looper.getMainLooper()).post {
            HTTPClient.retrofitAnimuDj().create(AnimuDjAPI::class.java)
                .findDJ()
                .enqueue(object : Callback<AnimuDjResponse> {
                    override fun onResponse(
                        call: Call<AnimuDjResponse>,
                        response: Response<AnimuDjResponse>
                    ) {
                        if (response.isSuccessful) {
                            val response = response.body()
                            val animuDjResponse =
                                AnimuDjResponse(
                                    response?.announcer ?: "Haruka Yuki",
                                    response?.program ?: "Animu NON-STOP",
                                    response?.liveOrders ?: "no",
                                    response?.image ?: "https://www.animu.com.br/wp-content/uploads/2023/07/Haru-chan-operator-dj-animu-2.webp",
                                    response?.orderAutoDJ ?: "sim"
                                )

                            Log.i("TAG", "onResponse: ${response?.announcer}")
                            callback.onSuccess(animuDjResponse)
                        } else {
                            Log.i("TAG", "onResponseError: ${response.body()}")
                            callback.onError(response.errorBody().toString())
                        }
                    }

                    override fun onFailure(call: Call<AnimuDjResponse>, t: Throwable) {
                        Log.i("TAG", "onResponse: ${t.message}")
                        callback.onError(t.message.toString())
                    }

                })
        }
    }
}