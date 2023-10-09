package catrine.dev.smarthouse.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import catrine.dev.smarthouse.data.remotedatasource.RemoteRepository
import catrine.dev.smarthouse.model.ModelState
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

abstract class SmartHouseViewModel<T> : ViewModel() where T : RealmObject {
    abstract val realm: Realm
    abstract val remoteRepository: RemoteRepository
    private var lastResult: RealmResults<*>? = null
    protected val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
    )
    protected val statesLiveData: MutableLiveData<ModelState> = MutableLiveData()

    fun loadRemoteData() {
        viewModelCoroutineScope.launch {
            try {
                statesLiveData.postValue(ModelState.Loading)
                updateItems(remoteRepository.getAllItems())
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    abstract fun getRealmObjectClassName(): KClass<*>

    fun getLastSuccessResults() = lastResult
    fun subscribeStates(): MutableLiveData<ModelState> {

        return statesLiveData
    }

    protected fun loadOnStart() {

        viewModelCoroutineScope.launch {
            try {
                getItemsFromLocal()
                if (lastResult?.isEmpty() == true) loadRemoteData()
                subscribeLocalRealmChanges()
            } catch (e: Throwable) {
                handleError(e)
            }
        }
    }

    protected fun subscribeLocalRealmChanges() {
        // flow.collect() is blocking -- run it in a background context
        val job = CoroutineScope(Dispatchers.Default).launch {
            try {
                val itemsFlow = getAllItems()?.asFlow()
                val subscription = itemsFlow?.collect { changes: ResultsChange<*> ->
                    try {
                        when (changes) {
                            // UpdatedResults means this change represents an update/insert/delete operation
                            is UpdatedResults -> {
                                statesLiveData.postValue(ModelState.Success(changes.list)) // the full collection of objects
                            }

                            else -> {
                                // types other than UpdatedResults are not changes -- ignore them
                            }
                        }
                    } catch (e: Throwable) {
                        handleError(e)
                    }
                }
            } catch (e: Throwable) {
                handleError(e)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getAllItems(): RealmResults<*>? {
        return try {
            val ret = realm.query(getRealmObjectClassName() as KClass<RealmObject>)
                .sort("room")
                .find()

            // debug log output
            ret.forEach { item ->
                Log.d("Item:", item.toString())
            }
            ret
        } catch (e: Exception) {
            handleError(e)
            null
        }
    }

    protected inline fun <reified T> updateItemName(id: Int, name: String) where T : RealmObject {
        viewModelCoroutineScope.launch {
            try {
                updateName(id, name)
                getItemsFromLocal()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    abstract fun updateName(id: Int, name: String)

    protected inline fun <reified T> updateItems(items: List<T>) where T : RealmObject {
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
                getItemsFromLocal()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    protected fun getItemsFromLocal() {
        try {
            statesLiveData.postValue(getAllItems()?.let {
                lastResult = it
                ModelState.Success(it)
            })
        } catch (e: Exception) {
            handleError(e)
        }
    }

    fun handleError(error: Throwable) {
        try {
            // error log output
            Log.e(this::class.simpleName, "Captured Throwable", error)
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