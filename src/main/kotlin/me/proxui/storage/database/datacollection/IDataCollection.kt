package me.proxui.storage.database.datacollection

import me.proxui.storage.Savable
import me.proxui.storage.Storage

interface IDataCollection : Storage, Savable {
    fun close()
}