package me.proxui.storage.database.impl

import com.google.gson.Gson
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import me.proxui.storage.database.IDataCollection
import me.proxui.utils.logger
import org.bson.Document


@Suppress("UNCHECKED_CAST")
open class DataCollection internal constructor(val name: String, private val connection: MongoClient, private val database: MongoDatabase) : IDataCollection {

    companion object {
        private val gsonObject by lazy { Gson() }
    }

    private lateinit var cache: MutableMap<String, Any>
    private val collection: MongoCollection<Document> = let {
        if(!database.listCollectionNames().contains(name)) database.createCollection(name)
        database.getCollection(name)
    }

    init {
        reload()
        save()
    }

    override fun <T> get(key: String): T {
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

    final override fun save() {
        collection.insertOne(Document.parse(gsonObject.toJson(cache)))
    }

    final override fun reload() {
        cache = collection.find(MutableMap::class.java).first() as MutableMap<String, Any>? ?: mutableMapOf()
    }

    override fun containsKey(key: String) = cache.containsKey(key)

    override fun containsValue(value: Any) = cache.containsValue(value)

    override fun close() {
        connection.close()
    }
}