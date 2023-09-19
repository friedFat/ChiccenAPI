package me.proxui.utils

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.plugin.java.JavaPlugin

val logger; get() = Bukkit.getLogger()

fun pluginPermission(plugin: JavaPlugin, name: String) = Permission("${plugin.name}."+name)