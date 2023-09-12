package catrine.dev.smarthouse.viewmodels

import catrine.dev.smarthouse.model.Camera
import catrine.dev.smarthouse.data.remotedatasource.CameraDataSourceImpl
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class CamerasViewModel : SmartHouseViewModel<Camera>() {
    private val cameraDataSource = CameraDataSourceImpl()
    override val realm: Realm
        get() = camerasRealm
    private val camerasRealm: Realm = Realm.open(
        RealmConfiguration.create(
            schema = setOf(Camera::class)
        )
    )




}