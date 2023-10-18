package me.proxui._storage.database.mongo

import com.mongodb.ConnectionString
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.proxui._storage.Storage
import me.proxui._storage.datafile.DataFile
import me.proxui.structure.Configurations
import me.proxui.utils.logger
import me.proxui.utils.toMap
import org.bson.Document

class MongoDatabase(private val configs: Configurations, val name: String) : IMongoDatabase {

    private val connection: MongoClient
    private val database: MongoDatabase

    init {
        logger.info("Connecting to MongoDB database...")
        connection = MongoClients.create(ConnectionString(configs.databaseURL))

        try {
            this.database = this.connection.getDatabase(name)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Name is invalid")
        }
    }

    override fun getCollection(name: String): Storage {
        if(configs.saveLocally) return DataFile(configs.plugin, this.name + "." + name)

        if(!database.listCollectionNames().contains(name)) database.createCollection(name)
        val collection = database.getCollection(name)
        return MongoCollection(collection)
    }

    override fun close() = this.connection.close()
}

class MongoCollection(private val collection: MongoCollection<Document>) : Storage(Document()){
    init {
        reload()
    }

    override fun save() {
        collection.insertOne(Document(super.entries.toMap()))
    }

    override fun reload() {
        super.putAll(collection.find().first() ?: Document())
    }
}