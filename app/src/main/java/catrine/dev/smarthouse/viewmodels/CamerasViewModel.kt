package catrine.dev.smarthouse.viewmodels

import catrine.dev.smarthouse.model.Camera
import catrine.dev.smarthouse.data.remotedatasource.CameraDataSourceImpl
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class CamerasViewModel : SmartHouseViewModel<Camera>() {
    override fun getRealmObjectClassName() = Camera::class
    override val realm: Realm
        get() = camerasRealm
    override val remoteRepository = CameraDataSourceImpl()

    private val camerasRealm: Realm

    init {
        val config = RealmConfiguration.create(
            schema = setOf(Camera::class)
        )
        camerasRealm = Realm.open(config)

        loadOnStart()
    }

    override fun updateName(id: Int, name: String) {
        val item = realm.query(
            Camera::class,
            "id == $id"
        ).first().find()
        item?.name = name
    }
}