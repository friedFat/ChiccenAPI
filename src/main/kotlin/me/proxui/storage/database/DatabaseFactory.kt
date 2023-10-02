package me.proxui.storage.database

import me.proxui.storage.database.impl.DataCollection
import me.proxui.storage.datafile.DataFile
import org.bukkit.plugin.Plugin

class FakeDatabase internal constructor(plugin: Plugin, name: String) : DataFile(plugin, name), IDataCollection {
    override fun close() {}
}

fun DataCollection.Companion.create(database: IDatabase, name: String): IDataCollection {
    return if (database.configurations.inDev) FakeDatabase(database.configurations.plugin, name)
    else DataCollection(name, database.connection, database.database)
}