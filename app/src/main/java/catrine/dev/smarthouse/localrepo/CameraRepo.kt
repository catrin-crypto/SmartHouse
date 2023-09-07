package catrine.dev.smarthouse.localrepo

import catrine.dev.smarthouse.data.Camera

interface CameraRepo {
    fun getCameras() : List<Camera>

    fun appendCameras()

    fun updateCameraName(id: Int, name: String)
}