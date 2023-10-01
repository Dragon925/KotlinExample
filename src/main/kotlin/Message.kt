class Message(
    id: Int,
    val text: String,
    val authorId: Int,
    val time: Long,
    val state: State
) : Entity(id)