package me.proxui.storage.database

import me.proxui.storage.Savable
import me.proxui.storage.Storage

interface IDataCollection : Storage, Savable {
    fun close()
}