package me.proxui._storage.database.mongo

import me.proxui._storage.Storage
import me.proxui._storage.database.IDatabase

interface IMongoDatabase : IDatabase {
    fun getCollection(name: String): Storage
}