package catrine.dev.smarthouse.network

import android.util.Log
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

class NetErrorProcessor {
    companion object {
        fun processError(e: Exception) {
            when (e) {
                is RedirectResponseException -> {
                    Log.e("NetApi", "3XX Error: ${e.message}")
                }

                is ClientRequestException -> {
                    Log.e("NetApi", "4XX Error: ${e.message}")
                }

                is ServerResponseException -> {
                    Log.e("NetApi", "5XX Error: ${e.message}")

                }

                else -> {
                    Log.e("NetApi", "Error: ${e.message}")
                }
            }
        }
    }
}