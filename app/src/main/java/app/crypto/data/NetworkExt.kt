package app.crypto.data

import retrofit2.Response

fun <T> Response<T>.getNetworkResult(): Result<T> {
    return body()?.let {
        println(it)
        Success(it)
    } ?: errorBody()?.let {
        Failure(
            NetworkException(it.string())
        )
    } ?: Failure(
        Error("Unknown Network Error 🙉")
    )
}

class NetworkException(message: String): Exception(message)