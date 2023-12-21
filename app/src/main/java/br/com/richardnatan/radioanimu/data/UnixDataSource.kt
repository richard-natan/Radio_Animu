package br.com.richardnatan.radioanimu.data

import android.os.Handler
import android.os.Looper
import br.com.richardnatan.radioanimu.model.UnixTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UnixDataSource {

    fun getCurrentUnix(callback: UnixCallback) {
        Handler(Looper.getMainLooper()).post {
            HTTPClient.retrofitUnixTime().create(UnixAPI::class.java)
                .findUnix()
                .enqueue(object : Callback<UnixTime> {
                    override fun onResponse(call: Call<UnixTime>, response: Response<UnixTime>) {
                        if (response.isSuccessful) {
                            val unix = response.body()
                            callback.onSuccess(unix?.unixtime ?: 1000)
                        } else {
                            val error = response.errorBody().toString()
                            callback.onError(error ?: "ERRO INTERNO")
                        }
                    }

                    override fun onFailure(call: Call<UnixTime>, t: Throwable) {
                        val error = t.message.toString()
                        callback.onError(error)
                    }

                })
        }
    }
}