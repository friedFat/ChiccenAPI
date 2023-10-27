package me.proxui.storage.datafile

import com.google.gson.GsonBuilder
import me.proxui.storage.FileMode
import me.proxui.storage.Storage
import net.axay.kspigot.languageextensions.kotlinextensions.createIfNotExists
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

open class Datafile(plugin: Plugin, private val name: String, fileMode: FileMode) : Storage(plugin, fileMode) {

    private val file = File(plugin.dataFolder, "$name.json")

    init {
        file.createIfNotExists()
        this.reload()
    }

    override fun save() {
        try {
            val writer = FileWriter(file)
            writer.write(gsonObject.toJson(this.toMap()))

            writer.close()
        } catch (ex: IOException) {
            throw IOException("Failed to save $name.json: $ex")
        }
    }

    final override fun reload() {
        val content = gsonObject.fromJson<Map<String, Any>?>(FileReader(file), Map::class.java) ?: mapOf()
        this.clear()
        this.putAll(content)
    }

    companion object {
        private val gsonObject = GsonBuilder().setPrettyPrinting().create()
    }
}