import kotlin.random.Random

object Repository {

    private const val MIN_MESSAGE_PER_USER = 25
    private const val USER_COUNT = 10

    private val names = listOf("Vasya", "Alina", "Petr", "Ira", "Ivan", "Tanya", "Anton")
    private val texts = listOf("Hello!", "How are you?", "Bye", "Where are you?", "I'm fine", "Let's go somewhere", "I'm here")


    private fun generateUsers(count: Int): List<User> {
        val users = mutableListOf<User>()
        for (i in 0 until count) {
            val name = names.random()
            val url = if (Random.nextBoolean()) i.toString() else null
            val user = User(i, name + i, url)
            users.add(user)
        }
        return users
    }

    private fun generateMessages(usersCount: Int): Map<Int, List<Message>> {
        val messages = mutableMapOf<Int, List<Message>>()
        var messageId = 0
        for (i in 0 until usersCount) {
            val messagesList = mutableListOf<Message>()
            val messageCount = Random.nextInt(MIN_MESSAGE_PER_USER) + MIN_MESSAGE_PER_USER
            val states = generateState(messageCount, usersCount)
            for (j in 0 until messageCount) {
                val text = texts.random()
                val message = Message(messageId, text, i, System.currentTimeMillis(), states[j])
                messagesList.add(message)
                messageId++
            }
            messages[i] = messagesList
        }
        return messages
    }

    private fun generateState(messageCount: Int, usersCount: Int): List<State> = List(messageCount) {
        when(Random.nextInt(1_000_000) % 3) {
            0 -> State.Read
            1 -> State.Unread
            else -> State.Deleted(Random.nextInt(usersCount))
        }
    }

//    private fun generateState(messageCount: Int, usersCount: Int): List<State> {
//        val states = mutableListOf<State>()
//        for (i in 0 until messageCount) {
//            val state = when(Random.nextInt(1_000_000) % 3) {
//                0 -> State.Read
//                1 -> State.Unread
//                else -> State.Deleted(Random.nextInt(usersCount))
//            }
//            states.add(state)
//        }
//        return states
//    }

    private fun generateChat(usersCount: Int, messages: Map<Int, List<Message>>): List<Chat> {
        val userPairs = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        for (i in 0 until usersCount) {
            val messagesList = messages[i] ?: emptyList()
            for (message in messagesList) {
                val id = Random.nextInt(usersCount)
                val receiverId = if (id == i) (id + 1) % usersCount else id

                var userPair: Pair<Int, Int> = receiverId to message.authorId
//                if (!userPairs.containsKey(userPair)) {
//                    userPairs[userPair] = mutableListOf()
//                }
//                userPairs[userPair]?.add(message.id)
                userPairs.getOrPut(userPair) { mutableListOf() }.add(message.id)

                userPair = message.authorId to receiverId
                userPairs.getOrPut(userPair) { mutableListOf() }.add(message.id)
            }
        }
        var chatId = 0
        val chats = mutableListOf<Chat>()
        for ((key, value) in userPairs.entries) {
            val chat = Chat(chatId, members = key, messageIds = value)
            chats.add(chat)
            chatId++
        }

        return chats
    }

    fun generateEntities(): List<Entity> {
        val users = generateUsers(USER_COUNT)
        val usersToMessagesMap = generateMessages(USER_COUNT)
        val chats = generateChat(USER_COUNT, usersToMessagesMap)
        val messages = usersToMessagesMap.flatMap { it.value }

        val entities = mutableListOf<Entity>()
        entities.addAll(users)
        entities.addAll(chats)
        entities.addAll(messages)

        return entities.shuffled()
    }

}