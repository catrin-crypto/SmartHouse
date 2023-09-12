package catrine.dev.smarthouse.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import catrine.dev.smarthouse.model.SmartHouseRealmObject
import catrine.dev.smarthouse.model.ModelState
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

abstract class SmartHouseViewModel<T> : ViewModel() where T : SmartHouseRealmObject {

    abstract val realm : Realm

    protected val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
    )
    protected val statesLiveData: MutableLiveData<ModelState> = MutableLiveData()

    inline fun <reified T> getAllItems(): RealmResults<T>? where T : RealmObject{
        try {
            return realm.query(T::class).find()
        } catch (e: Exception) {
            handleError(e)
            return null
        }
    }

    protected inline fun <reified T>  updateItemName(id: Int, name: String) where T : SmartHouseRealmObject {
        viewModelCoroutineScope.launch {
            try {
                val camera = realm.query(
                    T::class,
                    "camera_id == $id"
                ).first().find()
                camera?.name = name
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    protected inline fun <reified T> updateItems(items: List<T>) where T : SmartHouseRealmObject {
        viewModelCoroutineScope.launch {
            try {
                realm.write {
                    for (item in items) {
                        this.copyToRealm(
                            item,
                            updatePolicy = UpdatePolicy.ALL
                        )
                    }
                }
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    fun handleError(error: Throwable) {
        try {
            statesLiveData.postValue(ModelState.Error(error))
        } catch (_: Throwable) {
        }
    }

    override fun onCleared() {
        try {
            super.onCleared()
            viewModelCoroutineScope.cancel()
            viewModelCoroutineScope.coroutineContext.cancelChildren()
            realm.close()
        } catch (t: Throwable) {
            handleError(t)
        }
    }
}