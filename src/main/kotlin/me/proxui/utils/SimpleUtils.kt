package me.proxui.utils

import net.axay.kspigot.commands.CommandContext
import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

val logger by lazy { Bukkit.getLogger() }

fun pluginPermission(plugin: JavaPlugin, name: String) = Permission("${plugin.name}."+name)

fun <T> MutableSet<T>.setContains(element: T, boolean: Boolean) {
    if(boolean) add(element)
    else remove(element)
}

fun <T> AbstractList<T>.setContains(element: T, boolean: Boolean) {
    if(boolean) this.add(element)
    else remove(element)
}

/**
 * Same as CommandContext#player
 */
val CommandContext.p; get() = player

fun <T> Collection<T>.withEach(action: T.() -> Unit) {
    for(element in this) { action(element) }
}