package catrine.dev.smarthouse.model


sealed class ModelState {
    class Success() : ModelState()
    data class Error(val error: Throwable) : ModelState()
}
