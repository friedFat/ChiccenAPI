package me.proxui.storage

interface SavableStorage : Storage {

    /**
     * Saves the file
     */
    fun save()

    /**
     * Reloads all values
     */
    fun reload()

    /**
     * Sets the value for the specified key and saves the storage
     */
    fun setAndSave(key: String, value: Any?) {
        set(key, value)
        save()
    }

    /**
     * Returns the value for the key as the type [T] or sets and returns [altValue] if the key was not found
     */
    fun <T> getOrSet(key: String, save: Boolean = false, altValue: () -> T): T {
        if (!containsKey(key)) {
            set(key, altValue())
            save()
        }
        return get<T>(key)!!
    }

    /**
     * Returns the value for the key as the type [T] or sets and returns [altValue] if no value was found
     */
    fun <T> getOrSet(key: String, altValue: T, save: Boolean = false): T = getOrSet(key) { altValue }

    fun register() {
        REGISTRY.add(this)
    }

    companion object {
        private val REGISTRY = mutableSetOf<SavableStorage>()
        fun getRegistry(): Set<SavableStorage> {
            return REGISTRY.toSet()
        }
    }
}