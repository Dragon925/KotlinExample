class Chat(
    id: Int,
    val members: Pair<Int, Int>,
    val messageIds: List<Int>
) : Entity(id)