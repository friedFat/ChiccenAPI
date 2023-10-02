package me.proxui.storage.datafile

import com.google.gson.GsonBuilder
import me.proxui.storage.SavableStorage
import me.proxui.utils.logger
import net.axay.kspigot.event.listen
import net.axay.kspigot.languageextensions.kotlinextensions.createIfNotExists
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.*


/**
 * Creates a .json data-file called [name].json
 */
open class DataFile(plugin: Plugin, val name: String, autoSave: Boolean = true) : SavableStorage {
    companion object {
        private val gsonObject by lazy { GsonBuilder().setPrettyPrinting().create() }
    }

    private val file = File(plugin.dataFolder, "$name.json")
    private lateinit var cache: SortedMap<String, Any>

    init {
        file.createIfNotExists()
        reload()

        if (autoSave) {
            register()
            listen<PluginDisableEvent> { e ->
                if (e.plugin != plugin) return@listen
                save()
            }
        }
    }

    override fun set(key: String, value: Any?) {
        if (value == null) cache.remove(key)
        else cache[key] = value
    }

    override fun <T> get(key: String, reload: Boolean): T? {
        if (reload) reload()
        try {
            @Suppress("UNCHECKED_CAST")
            return cache[key] as T?
        } catch (ex: ClassCastException) {
            logger.severe("Wrong type was found while retrieving data in the temp storage for $name.json: $ex")
        }
        return null
    }

    override fun save() {
        try {
            val writer = FileWriter(file)
            writer.write(gsonObject.toJson(cache))
            writer.close()
        } catch (ex: IOException) {
            throw RuntimeException("File '$name' is invalid. Failed to save: $ex")
        }
    }

    final override fun reload() {
        cache = (gsonObject.fromJson<Map<String, Any>?>(FileReader(file), Map::class.java) ?: mapOf())
            .toSortedMap(String.CASE_INSENSITIVE_ORDER)
    }

    final override fun register() { super.register() }

    override fun containsKey(key: String) = cache.containsKey(key)

    override fun containsValue(value: Any) = cache.containsValue(value)
}
