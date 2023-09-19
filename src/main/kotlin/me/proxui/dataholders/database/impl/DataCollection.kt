package me.proxui.dataholders.database.impl

import com.google.gson.Gson
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.proxui.dataholders.database.IDataCollection
import me.proxui.utils.logger
import org.bson.Document

interface DatabaseInfo {
    val host: String
    val port: Int
}


@Suppress("UNCHECKED_CAST")
open class DataCollection(val name: String, private val connection: MongoClient, private val database: MongoDatabase) :
    IDataCollection {

    companion object {
        private val gsonObject by lazy { Gson() }
    }
    private val collection: MongoCollection<Document> = let {
        if(!database.listCollectionNames().contains(name)) database.createCollection(name)
        database.getCollection(name)
    }
    private lateinit var content: MutableMap<String, Any>

    init {
        reload()
        save()
    }

    override fun <T> get(key: String): T {
        try {
            return content[key] as T
        } catch (ex: ClassCastException) {
            logger.severe("Unexpected data type in collection '$name' in database '$name': $ex")
            throw ex
        }
    }

    override fun set(key: String, any: Any?) {
        if (any == null) content.remove(key)
        else content[key] = any
    }

    override fun save() {
        collection.insertOne(Document.parse(gsonObject.toJson(content)))
    }

    override fun reload() {
        content = collection.find(MutableMap::class.java).first() as MutableMap<String, Any>? ?: mutableMapOf()
    }

    override fun close() {
        connection.close()
    }
}