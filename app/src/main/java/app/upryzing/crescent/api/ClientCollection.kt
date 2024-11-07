package app.upryzing.crescent.api

open class ClientCollection<T> {
    private val cache: MutableMap<String, T> = mutableMapOf()

    fun get(id: String): T? {
        return cache[id]
    }

    fun set(id: String, data: T) {
        cache[id] = data
    }

    fun invalidate(id: String) {
        cache.remove(id)
    }
}