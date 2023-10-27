package me.proxui.storage

import net.axay.kspigot.event.listen
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin

enum class FileMode {
    /**
     * Automatically reloads before getting and saving and saves after setting
     */
    DYNAMIC,

    /**
     * Automatically saves after setting a value, does not reload before.
     * This causes any changes, done by another Storage object, after the last reload in this storage object to be overwritten
     */
    AUTO_SAVE,

    /**
     * Saves when the plugin is shutting down and reloads on start
     * This causes any changes, done by another Storage object, after the last reload in this storage object to be overwritten
     */
    SAVE_ON_STOP,

    /**
     * Only loads the file once, does not save
     */
    LOAD_ONLY
}

@Suppress("UNCHECKED_CAST", "unused")
abstract class Storage(private val plugin: Plugin, private val fileMode: FileMode, map: Map<String, Any> = mapOf()) {
    private val cache = map.toMutableMap()

    init {
        this.reload()
        if (fileMode == FileMode.SAVE_ON_STOP) {
            listen<PluginDisableEvent> { event ->
                if (event.plugin != plugin) return@listen
                this.save()
            }
        }
    }

    abstract fun save()

    abstract fun reload()

    operator fun <T> get(key: String): T {
        this.onRetrieve()
        return cache[key] as T
    }

    operator fun <T> set(key: String, value: T): T {
        if (value == null) cache.remove(key)
        else cache[key] = value
        return value.also {
            onWrite()
        }
    }

    fun <T> getOrSet(key: String, alternative: T): T {
        if (!cache.containsKey(key)) this[key] = alternative
        return this[key]
    }

    fun <T> getOrSet(key: String, alternative: () -> T): T {
        if (!cache.containsKey(key)) this[key] = alternative()
        return this[key]
    }

    fun containsKey(key: String): Boolean {
        this.onRetrieve()

        return cache.containsKey(key)
    }

    fun containsValue(value: Any): Boolean {
        this.onRetrieve()
        return cache.containsValue(value)
    }

    fun putAll(map: Map<String, Any>) {
        cache.putAll(map)
        this.onWrite()
    }

    fun clear() {
        cache.clear()
        this.onWrite()
    }

    fun toMap() = cache.toMap()

    /**
     * @param dynamic, if the storage should be reloaded before accessing and saved after saving
     */
    fun <T> getKey(key: String, dynamic: Boolean = true) = StorageKey<T>(this, key, dynamic)

    private fun onWrite() {
        if (fileMode == FileMode.DYNAMIC || fileMode == FileMode.AUTO_SAVE) {
            save()
        }
    }

    private fun onRetrieve() {
        if (fileMode == FileMode.DYNAMIC) this.reload()
    }

    operator fun iterator() = cache.iterator()
}

class StorageKey<T>(storage: Storage, private val key: String, private val dynamic: Boolean) {

    val storage = storage
        get() {
            if(dynamic) storage.reload()
            return field
        }

    fun load() = storage.get<T>(key)

    fun save(value: T?) {
        storage.also {
            it[key] = value
        }.save()
    }

    fun loadOrSave(alternative: Any?) = storage.getOrSet(key, alternative)

    fun loadOrSave(alternative: () -> Any?) = storage.getOrSet(key, alternative)
}