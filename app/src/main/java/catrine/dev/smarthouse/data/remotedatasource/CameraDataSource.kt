package catrine.dev.smarthouse.data.remotedatasource

import catrine.dev.smarthouse.data.Camera

interface CameraDataSource {

    suspend fun getAllCameras() : List<Camera>

}