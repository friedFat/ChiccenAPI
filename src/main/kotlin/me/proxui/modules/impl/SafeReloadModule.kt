package me.proxui.modules.impl

import me.proxui.modules.workaround.Module
import me.proxui.structure.chiccenAPI
import net.axay.kspigot.event.listen
import org.bukkit.Bukkit
import org.bukkit.event.EventPriority
import org.bukkit.event.server.PluginDisableEvent

object SafeReloadModule : Module(chiccenAPI,"SafeReload") {
    override fun onInitialization() {
        listen<PluginDisableEvent>(priority = EventPriority.HIGHEST, ignoreCancelled = true) { it ->
            if(it.plugin.name != chiccenAPI.name) return@listen
            Bukkit.getOnlinePlayers().forEach {
                if(it.isWhitelisted) return@forEach
                it.kickPlayer("§cServer is reloading!")
            }
        }
    }
}