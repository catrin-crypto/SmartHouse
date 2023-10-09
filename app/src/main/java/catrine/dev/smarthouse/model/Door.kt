package catrine.dev.smarthouse.model

import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
class Door : RealmObject, SmartHouseEntity {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = -1

    @SerializedName("name")
    var name: String? = null

    @SerializedName("favorites")
    var favorites: Boolean? = null

    @SerializedName("room")
    var room: String? = null

    @SerializedName("snapshot")
    var snapshot: String? = null

    override fun getRoomName() = room

    /**
     * Функция вывода содержимого в отладку
     */
    override fun toString(): String {
        return "Door: name: ${name}, id: ${id}, ${favorites?.toString()}, room: ${room}, snapshot: ${snapshot}"
    }
}