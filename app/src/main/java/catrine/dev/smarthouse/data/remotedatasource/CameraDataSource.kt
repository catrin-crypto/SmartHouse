package catrine.dev.smarthouse.data.remotedatasource

import catrine.dev.smarthouse.model.Camera

interface CameraDataSource {

    suspend fun getAllCameras(): List<Camera>

}