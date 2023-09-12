package catrine.dev.smarthouse.viewmodels

import catrine.dev.smarthouse.model.Door
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class DoorsViewModel() : SmartHouseViewModel<Door>() {

    private val doorsRealm = Realm.open(RealmConfiguration.create(
        schema = setOf(Door::class)
    ))
    override val realm: Realm
        get() = doorsRealm

}