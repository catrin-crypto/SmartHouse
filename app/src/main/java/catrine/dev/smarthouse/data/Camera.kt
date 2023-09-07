package catrine.dev.smarthouse.data

import io.realm.kotlin.types.RealmObject
import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Camera(): RealmObject{
    @PrimaryKey
    var id: ObjectId = ObjectId()

    @SerializedName("name") var name: String? = null
    @SerializedName("snapshot") var snapshot: String? = null
    @SerializedName("room") var room: String? = null
    @SerializedName("id") var camera_id: Int = -1
    @SerializedName("favorites") var favorites: Boolean? = null
    @SerializedName("rec") var rec: Boolean? = null

    constructor(camera_id: Int) : this() {
        this.camera_id = camera_id
    }
}


