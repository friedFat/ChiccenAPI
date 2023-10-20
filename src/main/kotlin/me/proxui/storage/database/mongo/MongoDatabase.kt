package me.proxui.storage.database.mongo

import com.mongodb.ConnectionString
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import me.proxui.storage.Storage
import me.proxui.storage.datafile.Datafile
import me.proxui.structure.Configurations
import me.proxui.utils.logger
import net.axay.kspigot.event.listen
import org.bson.Document
import org.bukkit.event.server.PluginDisableEvent

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

        listen<PluginDisableEvent> { e ->
            if(e.plugin != configs.plugin) return@listen
            this.collections.forEach {
                it.save()
            }
        }
    }

    override fun getCollection(name: String): Storage {
        if(configs.saveLocally) return Datafile(configs.plugin, this.name + "." + name)

        if(!database.listCollectionNames().contains(name)) database.createCollection(name)
        val collection = database.getCollection(name)
        return MongoCollection(collection).also { collections.add(it) }
    }

    override fun close() = this.connection.close()
}

open class MongoCollection(private val collection: com.mongodb.client.MongoCollection<Document>) : Storage(Document()){

    init {
        reload()
    }

    override fun save() {
        collection.insertOne(Document(this))
    }

    final override fun reload() {
        super.putAll(collection.find().first() ?: Document())
    }
}