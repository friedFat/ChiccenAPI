package me.proxui.dataholders

interface DataHolder {

    /**
     * @param key - the path to the value in the config
     * @returns the value of key
     * @param <T> the type of the value expected
    */
    operator fun <T> get(key: String): T?

    /**
     * Returns the value for the key as the type [T] or sets and returns [altValue] if no value was found
     */
    fun <T> getOrSet(key: String, altValue: T): T = getOrSet(key) { altValue }

    /**
     * Returns the value for the key as the type [T] or sets and returns [altValue] if no value was found
     */
    fun <T> getOrSet(key: String, altValue: () -> T): T {
        return get<T>(key) ?: (altValue().also {
            set(key, it)
        })
    }

    /**
     * Sets the value for the specified key
     */
    operator fun set(key: String, any: Any?)

    /**
     * Saves the file
     */
    fun save() {}

    /**
     * Reloads all values
     */
    fun reload() {}
}
