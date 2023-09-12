package catrine.dev.smarthouse.model

import com.google.gson.annotations.SerializedName
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class SmartHouseRealmObject() : RealmObject {
    @PrimaryKey
    @SerializedName("id")
    var id: Int = -1
    @SerializedName("name")
    var name: String? = null
    @SerializedName("favorites")
    var favorites: Boolean? = null

    constructor(id: Int) : this() {
        this.id = id
    }
}