package me.proxui._storage.datafile

import com.google.gson.GsonBuilder
import me.proxui._storage.Storage
import me.proxui.structure.chiccenAPI
import org.bson.Document
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

open class DataFile(plugin: Plugin, private val name: String) : Storage(Document()) {

    companion object {
        private val gsonObject = GsonBuilder().setPrettyPrinting().create()
    }

    private val file = File(plugin.dataFolder, "$name.json")

    init {
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
        val content = gsonObject.fromJson<Map<out String, *>?>(FileReader(file), Map::class.java)
        super.putAll(content)
    }
}