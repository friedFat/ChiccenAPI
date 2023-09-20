package me.proxui.dataholders.database.impl

import com.mongodb.MongoClient
import me.proxui.dataholders.database.DatabaseConfiguration
import me.proxui.dataholders.database.IDatabase

class Database(databaseInfo: DatabaseConfiguration, name: String) : IDatabase {
    private val connection: MongoClient = MongoClient(databaseInfo.host, databaseInfo.port)
    private val database = connection.getDatabase(name)

    override fun close() = connection.close()

    override fun getCollection(name: String): DataCollection {
        return DataCollection(name, connection, database)
    }
}