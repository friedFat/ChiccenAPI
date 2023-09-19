package me.proxui.dataholders.datafile

import com.google.gson.GsonBuilder
import me.proxui.dataholders.DataHolder
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
class DataFile(plugin: Plugin, val name: String) : DataHolder {
    companion object {
        private val gsonObject by lazy { GsonBuilder().setPrettyPrinting().create() }
        val REGISTERED_DATA_FILES = mutableListOf<DataFile>()
    }

    private val file = File(plugin.dataFolder, "$name.json")
    private lateinit var tempStorage: SortedMap<String, Any>

    init {
        file.createIfNotExists()
        reload()
        REGISTERED_DATA_FILES.add(this)

        listen<PluginDisableEvent> { e ->
            if (e.plugin != plugin) return@listen
            save()
        }
    }

    override operator fun set(key: String, any: Any?) {
        if(any == null) tempStorage.remove(key)
        else tempStorage[key] = any
    }

    override fun <T> get(key: String): T? {
        if (tempStorage.containsKey(key)) {
            try {
                @Suppress("UNCHECKED_CAST")
                return tempStorage[key] as T
            } catch (ex: ClassCastException) {
                logger.severe("Wrong type was found while retrieving data in the temp storage for $name.json: $ex")
            }
            return null
        }
        return null
    }

    override fun save() {
        try {
            val writer = FileWriter(file)
            writer.write(gsonObject.toJson(tempStorage))
            writer.close()
        } catch (ex: IOException) {
            throw RuntimeException("File '$name' is invalid. Failed to save: $ex")
        }
    }

    override fun reload() {
        tempStorage = loadTempStorage()
    }

    private fun loadTempStorage() = gsonObject.fromJson<Map<String, Any>>(FileReader(file), Map::class.java).toSortedMap(String.CASE_INSENSITIVE_ORDER)
}
