package app.crypto.data

import retrofit2.Response
import java.io.IOException

@Throws(IOException::class)
fun <T> Response<T>.getNetworkResult(): T {
    return body()?.let {
        println(it)
        it
    } ?: errorBody()?.let {
        throw NetworkException(it.string())
    } ?: throw IOException("Unknown Network Error 🙉")
}

class NetworkException(message: String): IOException(message)