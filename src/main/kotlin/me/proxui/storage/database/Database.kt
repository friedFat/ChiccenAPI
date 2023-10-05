package me.proxui.storage.database

import com.mongodb.ConnectionString
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import me.proxui.storage.database.datacollection.DataCollection
import me.proxui.storage.database.datacollection.IDataCollection
import me.proxui.structure.Configurations

class Database(override val configurations: Configurations, name: String) : IDatabase {

    override val connection: MongoClient = MongoClients.create(ConnectionString(configurations.databaseURL))
    override val database: MongoDatabase = connection.getDatabase(name)

    override fun close() = connection.close()

    override fun getCollection(name: String, autoSave: Boolean): IDataCollection {
        return DataCollection.create(configurations.plugin, this, name, autoSave)
    }
}