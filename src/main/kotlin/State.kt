sealed class State {

    object Read : State()
    object Unread : State()
    data class Deleted(val id: Int) : State()
    object Updating : State()
}