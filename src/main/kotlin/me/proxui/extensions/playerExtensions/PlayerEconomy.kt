package me.proxui.extensions.playerExtensions

import me.proxui.events.PlayerBalanceUpdateEvent
import me.proxui.structure.chiccenAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer


private val dataCollection by lazy { chiccenAPI.database.getCollection("economy") }
var OfflinePlayer.balance: Int
    get() {
        dataCollection.reload()
        return dataCollection.getInteger(this.uniqueId.toString())
    }
    set(value) {
        val event = PlayerBalanceUpdateEvent(this, value)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return

        dataCollection.let {
            it[uniqueId.toString()] = event.newBalance
            it.save()
        }
    }