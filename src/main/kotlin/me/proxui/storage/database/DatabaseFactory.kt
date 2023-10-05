package me.proxui.storage.database

import me.proxui.storage.database.datacollection.IDataCollection
import me.proxui.storage.database.datacollection.DataCollection
import me.proxui.storage.datafile.DataFile
import org.bukkit.plugin.Plugin

class FakeDatabase internal constructor(plugin: Plugin, name: String) : DataFile(plugin, name), IDataCollection {
    override fun close() {}
}

fun DataCollection.Companion.create(plugin: Plugin, database: IDatabase, name: String, autoSave: Boolean = true): IDataCollection {
    return if (database.configurations.useDatabase) FakeDatabase(database.configurations.plugin, "db_$name")
    else DataCollection(plugin, name, database.connection, database.database, autoSave)
}