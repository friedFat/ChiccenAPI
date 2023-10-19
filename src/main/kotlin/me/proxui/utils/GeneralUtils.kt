package me.proxui.utils

import net.axay.kspigot.commands.CommandContext
import net.axay.kspigot.event.listen
import org.bukkit.Bukkit
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.permissions.Permission
import org.bukkit.plugin.Plugin

val logger; get() = Bukkit.getLogger()

fun pluginPermission(plugin: Plugin, name: String) = Permission("${plugin.name}." + name)

fun <T> MutableSet<T>.setContains(element: T, boolean: Boolean) {
    if (boolean) add(element)
    else remove(element)
}

fun <T> MutableList<T>.setContains(element: T, boolean: Boolean) {
    if (boolean) this.add(element)
    else this.remove(element)
}

/**
 * Same as CommandContext#player
 */
val CommandContext.p; get() = player


//Just a simple function definition
inline fun <reified T> cancelEvent(crossinline shouldCancel: (T) -> Boolean = { true }) where T : Event, T : Cancellable {
    listen<T> {
        if (shouldCancel(it)) (it as Cancellable).isCancelled = true
    }
}