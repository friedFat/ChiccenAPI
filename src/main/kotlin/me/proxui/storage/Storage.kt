package me.proxui.storage

interface Storage {

    /**
     * @param key - the path to the value in the config
     * @returns the value of key
     * @param <T> the type of the value expected
     * @param reload if the file should be reloaded, before getting the value
     */
    operator fun <T> get(key: String, reload: Boolean = false): T?

    /**
     * Returns the value for the key as the type [T] or sets and returns [altValue] if the key was not found
     */
    fun <T> getOrSet(key: String, altValue: T): T {
        if(!containsKey(key)) set(key, altValue)
        return altValue
    }

    /**
     * Returns the value for the key as the type [T] or sets and returns [altValue] if the key was not found
     */
    fun <T> getOrSet(key: String, altValue: () -> T): T {
        if (!containsKey(key)) set(key, altValue())
        return get<T>(key)!!
    }

    //this is kinda useless, because of elvis operator
    fun <T> getOrDefault(key: String, altValue: T): T {
        return if (containsKey(key)) get(key)!! else altValue
    }

    //this is also kinda useless, also because of elvis operator
    fun <T> getOrDefault(key: String, altValue: () -> T): T {
        return if (containsKey(key)) get(key)!! else altValue()
    }

    /**
     * Sets the value for the specified key
     */
    operator fun set(key: String, value: Any?)

    /**
     * @returns if the key is valid or not
     */
    fun containsKey(key: String): Boolean

    /**
     * @returns if that value is saved somewhere in the storage/cache
     */
    fun containsValue(value: Any): Boolean
}
