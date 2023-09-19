package me.proxui.player

import me.proxui.structure.database
import org.bukkit.entity.Player

private val playerStylingRegistry = database.getCollection("playerStyling").getOrSet("playerStyling") { mutableMapOf<String, PlayerStyling>() }
val Player.styling; get() = playerStylingRegistry[this.uniqueId.toString()] ?: {
    PlayerStyling().also {
        playerStylingRegistry[this.uniqueId.toString()] = it
    }
}

class PlayerStyling {
    var nameColor = 'f'
    var messageColor = '7'
    val prefixes = mutableListOf<String>()
    val suffixes = mutableListOf<String>()

    fun addPrefix(prefix: String) = prefixes.add(prefix)
    fun addSuffix(suffix: String) = prefixes.add(suffix)
    fun getPrefixesAsString() = prefixes.joinToString(" ").let { if(it.isBlank()) "" else "$it " }
    fun getSuffixesAsString() = prefixes.joinToString(" ").let { if(it.isBlank()) "" else " $it" }
}