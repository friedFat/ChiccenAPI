package me.proxui.storage

interface Storage {

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
        if (!this.containsKey(key)) set(key, altValue())
        return get<T>(key)!!
    }

    /**
     * Sets the value for the specified key
     */
    operator fun set(key: String, value: Any?)

    /**
     * Saves the file
     */
    fun save() {}

    /**
     * Reloads all values
     */
    fun reload() {}

    /**
     * @returns if the key is valid or not
     */
    fun containsKey(key: String) : Boolean

    /**
     * @returns if that value is saved somewhere in the dataholder
     */
    fun containsValue(value: Any): Boolean
}
