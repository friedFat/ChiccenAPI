package me.proxui.extensions.worldExtensions

import me.proxui.storage.datafile.DataFile
import me.proxui.structure.chiccenAPI
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.server
import net.kyori.adventure.text.Component
import org.bukkit.Server
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

enum class CustomGameRule(val default: Boolean) {
    DISABLE_HUNGER(false),
    DISABLE_DAMAGE(false),

    /**
     * Also disabled leave messages
     */
    DISABLE_JOIN_MESSAGES(false);

    val key = name.lowercase()
}

internal object CustomGameRuleManager {
    val data by lazy {
        DataFile(chiccenAPI, "customGameRules").apply {
            CustomGameRule.values().forEach { getOrSet(it.key, it.default, true) }
        }
    }

    init {
        listen<FoodLevelChangeEvent> {
            if (!server.getCustomGameRule(CustomGameRule.DISABLE_HUNGER)) return@listen
            it.entity.saturation = 20f
            it.isCancelled = true
        }

        listen<EntityDamageEvent> {
            if (!server.getCustomGameRule(CustomGameRule.DISABLE_DAMAGE)) return@listen
            it.isCancelled = true
        }

        listen<PlayerJoinEvent> {
            if (!server.getCustomGameRule(CustomGameRule.DISABLE_JOIN_MESSAGES)) return@listen
            it.joinMessage(Component.empty())
        }
        listen<PlayerQuitEvent> {
            if (!server.getCustomGameRule(CustomGameRule.DISABLE_JOIN_MESSAGES)) return@listen
            it.quitMessage(Component.empty())
        }
    }
}

/**
 * @returns the value of the custom game rule
 */
fun Server.getCustomGameRule(rule: CustomGameRule): Boolean {
    return CustomGameRuleManager.data.getOrSet(rule.key, rule.default)
}

/**
 * Sets the value of the custom game rule
 */
fun Server.setCustomGameRule(rule: CustomGameRule, value: Boolean) {
    CustomGameRuleManager.data[rule.key] = value
}