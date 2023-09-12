package catrine.dev.smarthouse.data.remotedatasource

import catrine.dev.smarthouse.model.Door

interface DoorDataSource {
    suspend fun getAllDoors(): List<Door>
}