package me.proxui.utils

import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.plugin.java.JavaPlugin

val logger; get() = Bukkit.getLogger()

fun pluginPermission(plugin: JavaPlugin, name: String) = Permission("${plugin.name}."+name)

fun <T> MutableSet<T>.setContains(element: T, boolean: Boolean) {
    if(boolean) this.add(element)
    else this.remove(element)
}