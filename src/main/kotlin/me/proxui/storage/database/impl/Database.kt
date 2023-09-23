package me.proxui.storage.database.impl

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoDatabase
import me.proxui.storage.database.IDataCollection
import me.proxui.storage.database.IDatabase
import me.proxui.storage.database.create
import me.proxui.structure.Configurations

class Database(override val configurations: Configurations, name: String, connectionTimeout: Int = 3000) : IDatabase {

    private val dbConfigs = configurations.databaseConfiguration
    override val connection = MongoClient(ServerAddress(dbConfigs.host, dbConfigs.port), MongoCredential.createCredential(dbConfigs.username,  name, dbConfigs.password.toCharArray()), MongoClientOptions.builder().connectTimeout(connectionTimeout).build())
    override val database: MongoDatabase = connection.getDatabase(name)

    override fun close() = connection.close()

    override fun getCollection(name: String): IDataCollection {
        return DataCollection.create(this, name)
    }
}