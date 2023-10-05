package me.proxui.storage.database

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import me.proxui.storage.database.datacollection.IDataCollection
import me.proxui.structure.Configurations

interface IDatabase {

    val configurations: Configurations
    val connection: MongoClient
    val database: MongoDatabase

    /**
     * @returns the collection with [name] and creates a new one if non-existent
     */
    fun getCollection(name: String, autoSave: Boolean = true) : IDataCollection

    /**
     * Closes the connection to the database server
     **/
    fun close()
}