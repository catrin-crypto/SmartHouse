package catrine.dev.smarthouse.data.remotedatasource

import io.realm.kotlin.types.RealmObject

interface RemoteRepository {
    suspend fun getAllItems(): List<RealmObject>
}