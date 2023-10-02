package me.proxui.system

import me.proxui.structure.ChiccenAPI
import me.proxui.structure.database
import net.axay.kspigot.extensions.bukkit.plainText
import org.bukkit.Bukkit

val ChiccenAPI.messages by lazy {
    database.getCollection("messages").getOrSet("messages") {
        object : Messages {
            override val noPermissionMsg = Bukkit.permissionMessage().plainText()
        }
    }
}

interface Messages {
    val noPermissionMsg: String
}