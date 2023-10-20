package me.proxui.storage.datafile

import com.google.gson.GsonBuilder
import me.proxui.storage.Storage
import net.axay.kspigot.event.listen
import net.axay.kspigot.languageextensions.kotlinextensions.createIfNotExists
import org.bson.Document
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

open class Datafile(plugin: Plugin, private val name: String) : Storage(Document()) {

    private val file = File(plugin.dataFolder, "$name.json")

    init {
        file.createIfNotExists()
        this.reload()

        listen<PluginDisableEvent> {
            if(it.plugin != plugin) return@listen
            this.save()
        }
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
        val content = gsonObject.fromJson<Map<out String, *>?>(FileReader(file), Map::class.java) ?: mapOf<String, Any?>()
        super.putAll(content)
    }

    companion object {
        private val gsonObject = GsonBuilder().setPrettyPrinting().create()
    }
}