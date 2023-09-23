package me.proxui.storage.database

import me.proxui.storage.Storage

interface IDataCollection : Storage {
    fun close()
}