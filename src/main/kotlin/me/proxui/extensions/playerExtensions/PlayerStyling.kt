package me.proxui.extensions.playerExtensions

import me.proxui._storage.getOrSet
import me.proxui.structure.chiccenAPI
import org.bukkit.entity.Player

private val playerStylingRegistry = chiccenAPI.database.getCollection("playerStyling").getOrSet("playerStyling") { mutableMapOf<String, PlayerStyling>() }
var Player.styling
    get() = playerStylingRegistry.getOrPut(uniqueId.toString()) { PlayerStyling() }
    set(value) {
        playerStylingRegistry[uniqueId.toString()] = value
    }

open class PlayerStyling {
    var nameColor = 'f'
    var messageColor = '7'
    var prefixes = mutableListOf<String>()
    var suffixes = mutableListOf<String>()

    fun getPrefixesAsString() = prefixes.joinToString(" ").let { if (it.isBlank()) "" else "$it " }
    fun getSuffixesAsString() = suffixes.joinToString(" ").let { if (it.isBlank()) "" else " $it" }
}