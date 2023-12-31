package br.com.richardnatan.radioanimu.data

import br.com.richardnatan.radioanimu.model.AnimuApiResponse

interface MusicCallback {

    fun onSuccess(response: AnimuApiResponse)

    fun onError(response: String)

    fun onComplete()
}