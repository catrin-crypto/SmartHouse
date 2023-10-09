package catrine.dev.smarthouse.data.remotedatasource


import catrine.dev.smarthouse.model.Door
import catrine.dev.smarthouse.network.ApiRoutes
import catrine.dev.smarthouse.network.KtorClient
import catrine.dev.smarthouse.network.NetErrorProcessor
import catrine.dev.smarthouse.network.Responses
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class DoorDataSourceImpl : DoorDataSource, RemoteRepository {
    override suspend fun getAllDoors(): List<Door> =
        KtorClient.client.use {
            try {
                val response: Responses.DoorResponse = it.get { url(ApiRoutes.DOORS) }.body()
                response.getDoors()
            } catch (e: Exception) {
                NetErrorProcessor.processError(e)
                throw (e)
            }
        }

    override suspend fun getAllItems() = getAllDoors()
}