package me.proxui.storage.database.impl

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import me.proxui.storage.database.IDataCollection
import me.proxui.storage.database.IDatabase
import me.proxui.storage.database.create
import me.proxui.structure.Configurations

class Database(override val configurations: Configurations, name: String) : IDatabase {

    override val connection: MongoClient = MongoClients.create(configurations.databaseURL)
    override val database: MongoDatabase = connection.getDatabase(name)

    override fun close() = connection.close()

    override fun getCollection(name: String, autoSave: Boolean): IDataCollection {
        return DataCollection.create(configurations.plugin, this, name, autoSave)
    }
}