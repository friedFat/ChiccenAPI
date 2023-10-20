package me.proxui.storage.database.mongo

import me.proxui.storage.Storage
import me.proxui.storage.database.IDatabase

interface IMongoDatabase : IDatabase {
    fun getCollection(name: String): Storage
}