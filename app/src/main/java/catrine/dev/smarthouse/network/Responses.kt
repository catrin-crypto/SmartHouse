package catrine.dev.smarthouse.network

import catrine.dev.smarthouse.model.Camera
import catrine.dev.smarthouse.model.Door
import com.google.gson.annotations.SerializedName

class Responses {
    data class CameraResponse(
        @SerializedName("success" ) var success : Boolean? = null,
        @SerializedName("data"    ) var data    : Data?    = Data()
    ){
        fun getCameras() : List<Camera> = data?.cameras?.toList() ?: emptyList()
    }

    data class Data (
        @SerializedName("room"    ) var room    : ArrayList<String>  = arrayListOf(),
        @SerializedName("cameras" ) var cameras : ArrayList<Camera> = arrayListOf()

    )

    data class DoorResponse(
        @SerializedName("success" ) var success : Boolean?        = null,
        @SerializedName("data"    ) var data    : ArrayList<Door> = arrayListOf()
    ){
        fun getDoors() : List<Door> = data
    }
}