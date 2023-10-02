package me.proxui.extensions.playerExtensions

import me.proxui.storage.database.create
import me.proxui.storage.database.impl.DataCollection
import me.proxui.structure.database
import org.bukkit.OfflinePlayer


private val dataCollection by lazy { DataCollection.create(database, "economy") }
var OfflinePlayer.balance: Int
    get() = dataCollection.let {
        it.reload()
        it.get<Int>(uniqueId.toString()) ?: 0
    }
    set(value) {
        dataCollection.let {
            it[uniqueId.toString()] = value
            it.save()
        }
    }