package me.proxui.system

import me.proxui.structure.ChiccenAPI
import net.axay.kspigot.extensions.bukkit.plainText
import org.bukkit.Bukkit

private val messages by lazy { object: Messages {
    override val noPermissionMsg = Bukkit.permissionMessage().plainText()
}}

val ChiccenAPI.messages: Messages
    get() = me.proxui.system.messages

interface Messages {
    val noPermissionMsg: String
}