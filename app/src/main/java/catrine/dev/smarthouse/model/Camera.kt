package catrine.dev.smarthouse.model

import com.google.gson.annotations.SerializedName

class Camera : SmartHouseRealmObject() {
    @SerializedName("snapshot")
    var snapshot: String? = null
    @SerializedName("room")
    var room: String? = null
    @SerializedName("rec")
    var rec: Boolean? = null
}


