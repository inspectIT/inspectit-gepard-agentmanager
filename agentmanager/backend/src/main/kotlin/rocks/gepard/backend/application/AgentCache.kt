package rocks.gepard.backend.application


class AgentCache<K, V> : GenericCache<K, V>  {

    private val cache = HashMap<K,V>()

    override val size: Int
        get() = cache.size

    override fun set(key: K, value: V) {
        cache[key] = value
    }

    override fun remove(key: K) = cache.remove(key)

    override fun get(key: K) = cache[key]

    override fun clear() = cache.clear()

    fun getAllAgents() : String{
        return cache.toString();
    }

}