package catrine.dev.smarthouse.data.remotedatasource

import android.util.Log
import catrine.dev.smarthouse.model.Camera
import catrine.dev.smarthouse.network.ApiRoutes
import catrine.dev.smarthouse.network.KtorClient
import catrine.dev.smarthouse.network.NetErrorProcessor
import catrine.dev.smarthouse.network.Responses
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url

class CameraDataSourceImpl: CameraDataSource {
    override suspend fun getAllCameras() : List<Camera> =
        KtorClient.client.use{
            try {
                val response : Responses.CameraResponse = it.get {  url(ApiRoutes.CAMERAS)}.body()
                response.getCameras()
             } catch (e: Exception) {
               NetErrorProcessor.processError(e)
               emptyList()
            }
        }

}