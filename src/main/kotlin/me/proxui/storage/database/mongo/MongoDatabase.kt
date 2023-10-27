package me.proxui.storage.database.mongo

import com.mongodb.BasicDBObject
import com.mongodb.ConnectionString
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import me.proxui.storage.FileMode
import me.proxui.storage.Storage
import me.proxui.storage.datafile.Datafile
import me.proxui.structure.Configurations
import me.proxui.utils.logger
import org.bson.Document
import org.bukkit.plugin.Plugin

open class MongoDatabase(private val configs: Configurations, val name: String) : IMongoDatabase {

    private val connection: MongoClient
    private val database: MongoDatabase
    private val collections = mutableSetOf<MongoCollection>()

    init {
        logger.info("Connecting to MongoDB database...")
        connection = MongoClients.create(ConnectionString(configs.databaseURL))

        try {
            this.database = this.connection.getDatabase(name)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Name is invalid")
        }
    }

    override fun getCollection(name: String, fileMode: FileMode): Storage {
        if (configs.saveLocally) return Datafile(configs.plugin, this.name + "." + name, fileMode)

        if (!database.listCollectionNames().contains(name)) database.createCollection(name)
        val collection = database.getCollection(name)
        return MongoCollection(configs.plugin, collection, fileMode).also { collections.add(it) }
    }

    override fun close() = this.connection.close()
}

open class MongoCollection(plugin: Plugin, private val collection: com.mongodb.client.MongoCollection<Document>, fileMode: FileMode) : Storage(plugin, fileMode) {

    override fun save() {
        collection.deleteMany(BasicDBObject())
        collection.insertOne(Document(this.toMap()))
    }

    final override fun reload() {
        super.clear()
        super.putAll(collection.find().first() ?: Document())
    }
}