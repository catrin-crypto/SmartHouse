package catrine.dev.smarthouse.network

import catrine.dev.smarthouse.data.Camera
import catrine.dev.smarthouse.data.Door
import com.google.gson.annotations.SerializedName

class Responses {
    data class CameraResponse(
        @SerializedName("success" ) var success : Boolean? = null,
        @SerializedName("data"    ) var data    : Data?    = Data()
    )

    data class Data (
        @SerializedName("room"    ) var room    : ArrayList<String>  = arrayListOf(),
        @SerializedName("cameras" ) var cameras : ArrayList<Camera> = arrayListOf()

    )

    data class DoorResponse(
        @SerializedName("success" ) var success : Boolean?        = null,
        @SerializedName("data"    ) var data    : ArrayList<Door> = arrayListOf()
    )
}