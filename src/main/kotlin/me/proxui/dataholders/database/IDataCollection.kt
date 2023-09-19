package me.proxui.dataholders.database

import me.proxui.dataholders.DataHolder

interface IDataCollection : DataHolder {
    fun close()
}