package catrine.dev.smarthouse.model

import com.google.gson.annotations.SerializedName

class Door : SmartHouseRealmObject() {

    @SerializedName("room")
    var room: String? = null

}