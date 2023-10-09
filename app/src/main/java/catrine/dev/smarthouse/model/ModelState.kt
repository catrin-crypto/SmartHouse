package catrine.dev.smarthouse.model

import io.realm.kotlin.query.RealmResults


sealed class ModelState {
    object Loading : ModelState()
    class Success(val realmResult: RealmResults<*>) : ModelState()
    data class Error(val error: Throwable) : ModelState()
}
