package me.proxui.storage

interface Savable : Storage {

    /**
     * Saves the file
     */
    fun save()

    /**
     * Reloads all values
     */
    fun reload() : Savable

    fun register() {
        REGISTRY.add(this)
    }

    companion object {
        private val REGISTRY = mutableSetOf<Savable>()
        fun getRegistry(): Set<Savable> {
            return REGISTRY.toSet()
        }
    }
}