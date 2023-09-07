package catrine.dev.smarthouse.data.remotedatasource

import catrine.dev.smarthouse.data.Camera
import catrine.dev.smarthouse.network.ApiRoutes
import catrine.dev.smarthouse.network.KtorClient
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
                response.data?.cameras?.toList() ?: emptyList()
            } catch (ex: RedirectResponseException) {
                // 3xx - responses
                println("Error: ${ex.response.status.description}")
                emptyList()
            } catch (ex: ClientRequestException) {
                // 4xx - responses
                println("Error: ${ex.response.status.description}")
                emptyList()
            } catch (ex: ServerResponseException) {
                // 5xx - response
                println("Error: ${ex.response.status.description}")
                emptyList()
            }
        }

}