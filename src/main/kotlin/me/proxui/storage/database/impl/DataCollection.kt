package me.proxui.storage.database.impl

import com.google.gson.Gson
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.proxui.storage.database.IDataCollection
import me.proxui.utils.logger
import net.axay.kspigot.event.listen
import org.bson.Document
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.Plugin


@Suppress("UNCHECKED_CAST")
class DataCollection internal constructor(plugin: Plugin, val name: String, private val connection: MongoClient, private val database: MongoDatabase, autoSave: Boolean = true) : IDataCollection {

    companion object {
        private val gsonObject by lazy { Gson() }
    }

    private lateinit var cache: MutableMap<String, Any>
    private val collection: MongoCollection<Document> = let {
        if (!database.listCollectionNames().contains(name)) database.createCollection(name)
        database.getCollection(name)
    }

    init {
        reload()


        listen<PluginDisableEvent> { e ->
            if (e.plugin != plugin) return@listen
            save()
        }
        if (autoSave) register()
        listen<PluginDisableEvent> { e ->
            if (e.plugin != plugin) return@listen

            if(autoSave) save()
            close()
        }
    }

    override fun <T> get(key: String, reload: Boolean): T {
        if (reload) reload()
        try {
            return cache[key] as T
        } catch (ex: ClassCastException) {
            logger.severe("Unexpected data type in collection '$name' in database '$name': $ex")
            throw ex
        }
    }

    override fun set(key: String, value: Any?) {
        if (value == null) cache.remove(key)
        else cache[key] = value
    }

    override fun save() {
        collection.deleteMany(Document())
        collection.insertOne(Document.parse(gsonObject.toJson(cache)))
    }

    override fun reload(): DataCollection {
        cache = collection.find(MutableMap::class.java).first() as MutableMap<String, Any>? ?: mutableMapOf()
        return this
    }

    override fun containsKey(key: String) = cache.containsKey(key)

    override fun containsValue(value: Any) = cache.containsValue(value)

    override fun close() {
        connection.close()
    }
}