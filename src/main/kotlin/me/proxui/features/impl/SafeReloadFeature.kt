package me.proxui.features.impl

import me.proxui.features.Feature
import me.proxui.structure.chiccenAPI
import net.axay.kspigot.event.listen
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.EventPriority
import org.bukkit.event.server.PluginDisableEvent

object SafeReloadFeature : Feature(chiccenAPI,"SafeReload") {
    override fun onInitialization() {
        listen<PluginDisableEvent>(priority = EventPriority.HIGHEST) { it ->
            if(it.plugin.name != chiccenAPI.name) return@listen
            Bukkit.getOnlinePlayers().forEach {
                if(it.isWhitelisted) return@forEach
                it.kick(Component.text("§cServer is reloading!"))
            }
        }
    }
}