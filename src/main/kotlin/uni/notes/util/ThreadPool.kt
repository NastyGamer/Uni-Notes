package uni.notes.util

class ThreadPool {

    private val threads = ArrayList<Thread>()

    fun add(thread: Thread) {
        thread.isDaemon = true
        threads.add(thread)
    }

    fun start() {
        Logger.info("Dispatching ${threads.size} threads")
        threads.forEach { thread -> thread.start() }
    }

    fun finished() = threads.none { thread -> thread.isAlive }
}