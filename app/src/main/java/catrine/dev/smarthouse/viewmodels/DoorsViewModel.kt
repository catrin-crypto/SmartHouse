package catrine.dev.smarthouse.viewmodels

import catrine.dev.smarthouse.data.remotedatasource.DoorDataSourceImpl
import catrine.dev.smarthouse.model.Door
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration


class DoorsViewModel : SmartHouseViewModel<Door>() {
    override val remoteRepository = DoorDataSourceImpl()
    override fun getRealmObjectClassName() = Door::class

    private val doorsRealm: Realm

    override val realm
        get() = doorsRealm

    init {
        val config = RealmConfiguration.create(
            schema = setOf(Door::class)
        )
        doorsRealm = Realm.open(config)

        loadOnStart()
    }

    override fun updateName(id: Int, name: String) {
        val item = realm.query(
            Door::class,
            "id == $id"
        ).first().find()
        item?.name = name
    }
}