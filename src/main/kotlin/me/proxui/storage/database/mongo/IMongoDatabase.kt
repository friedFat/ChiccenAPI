package me.proxui.storage.database.mongo

import me.proxui.storage.FileMode
import me.proxui.storage.Storage
import me.proxui.storage.database.IDatabase

interface IMongoDatabase : IDatabase {
    fun getCollection(name: String, fileMode: FileMode = FileMode.SAVE_ON_STOP): Storage
}