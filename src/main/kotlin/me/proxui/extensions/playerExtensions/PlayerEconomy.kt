package me.proxui.extensions.playerExtensions

import me.proxui.events.PlayerBalanceUpdateEvent
import me.proxui.storage.FileMode
import me.proxui.structure.chiccenAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer


private val economy by lazy { chiccenAPI.database.getCollection("economy", FileMode.DYNAMIC) }
var OfflinePlayer.balance: Int
    get() {
        return economy[this.uniqueId.toString()]
    }
    set(value) {
        val event = PlayerBalanceUpdateEvent(this, value)
        Bukkit.getPluginManager().callEvent(event)
        if (event.isCancelled) return

        economy[uniqueId.toString()] = event.newBalance
    }