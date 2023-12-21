package br.com.richardnatan.radioanimu.data

interface UnixCallback {
    fun onSuccess(response: Long)

    fun onError(response: String)
}