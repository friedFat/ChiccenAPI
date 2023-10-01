package me.proxui.storage.database

import me.proxui.storage.SavableStorage

interface IDataCollection : SavableStorage {
    fun close()
}