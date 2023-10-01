package me.proxui.system

import me.proxui.structure.ChiccenAPI
import me.proxui.structure.chiccenAPI
import net.axay.kspigot.extensions.bukkit.plainText
import org.bukkit.Bukkit

val ChiccenAPI.messages by lazy {
    chiccenAPI.database.getCollection("messages").getOrSet("messages") {
        object : Messages {
            override val noPermissionMsg = Bukkit.permissionMessage().plainText()
        }
    }
}

interface Messages {
    val noPermissionMsg: String
}