package me.proxui.utils

import net.axay.kspigot.commands.CommandContext
import net.axay.kspigot.event.listen
import org.bukkit.Bukkit
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.permissions.Permission
import org.bukkit.plugin.Plugin

val logger; get() = Bukkit.getLogger()


//Just a simple function definition
inline fun <reified T> cancelEvent(crossinline shouldCancel: (T) -> Boolean = { true }) where T : Event, T : Cancellable {
    listen<T> {
        if (shouldCancel(it)) (it as Cancellable).isCancelled = true
    }
}