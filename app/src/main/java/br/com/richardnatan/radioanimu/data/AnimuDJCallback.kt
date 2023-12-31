package br.com.richardnatan.radioanimu.data

import br.com.richardnatan.radioanimu.model.AnimuDjResponse

interface AnimuDJCallback {

    fun onSuccess(response: AnimuDjResponse)

    fun onError(response: String)

    fun onComplete()

}