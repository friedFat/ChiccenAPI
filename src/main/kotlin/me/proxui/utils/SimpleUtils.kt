package me.proxui.utils

import net.axay.kspigot.commands.CommandContext
import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass

val logger by lazy { Bukkit.getLogger() }

fun pluginPermission(plugin: JavaPlugin, name: String) = Permission("${plugin.name}."+name)

fun <T> MutableSet<T>.setContains(element: T, boolean: Boolean) {
    if(boolean) this.add(element)
    else this.remove(element)
}

/**
 * Same as CommandContext#player
 */
val CommandContext.p; get() = player
fun <T> CommandContext.getArgument(name: String, clazz: Class<T>): T = this.nmsContext.getArgument(name, clazz)
fun <T: Any> CommandContext.getArgument(name: String, clazz: KClass<T>): T = getArgument(name, clazz.java)