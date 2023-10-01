fun main(args: Array<String>) {
    val entities = Repository.generateEntities()

    for (entity in entities) {
        when(entity) {
            is User -> println(entity.name)
            is Message -> {
                println(entity.text)
                val state = when(entity.state) {
                    State.Read -> "read"
                    State.Unread -> "unread"
                    is State.Deleted -> "deleted by ${entity.state.id}"
                    State.Updating -> "updating"
                }
                println(state)
            }
            is Chat -> println(entity.members)
        }
    }
}