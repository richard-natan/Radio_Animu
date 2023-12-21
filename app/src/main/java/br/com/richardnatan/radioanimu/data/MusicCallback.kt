package br.com.richardnatan.radioanimu.data

import br.com.richardnatan.radioanimu.model.ApiResponse

interface MusicCallback {

    fun onSuccess(response: ApiResponse)

    fun onError(response: String)

    fun onComplete()
}