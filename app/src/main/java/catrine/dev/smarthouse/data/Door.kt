package catrine.dev.smarthouse.data

import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.RealmObject
import org.mongodb.kbson.ObjectId

class Door(

) : RealmObject {
    var _id: ObjectId = ObjectId()
    @SerializedName("name")
    var name: String? = null
    @SerializedName("room")
    var room: String? = null
    @SerializedName("id")
    var door_id: Int = -1
    @SerializedName("favorites")
    var favorites: Boolean? = null

    constructor(door_id: Int) : this() {
        this.door_id = door_id
    }
}