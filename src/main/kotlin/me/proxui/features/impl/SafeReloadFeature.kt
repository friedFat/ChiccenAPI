package me.proxui.features.impl

import me.proxui.features.Feature
import me.proxui.structure.chiccenAPI
import net.axay.kspigot.event.listen
import org.bukkit.Bukkit
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.PluginEnableEvent

object SafeReloadFeature : Feature("SafeReload") {
    override fun onInitialization() {
        listen<PluginDisableEvent> {
            if(it.plugin.name != chiccenAPI.name) return@listen
            Bukkit.setWhitelist(true)
        }
        listen<PluginEnableEvent> {
            if(it.plugin.name != chiccenAPI.name) return@listen
            Bukkit.setWhitelist(false)
        }
    }
}