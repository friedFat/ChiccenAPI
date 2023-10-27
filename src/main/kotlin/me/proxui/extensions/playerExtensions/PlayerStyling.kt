package me.proxui.extensions.playerExtensions

import me.proxui.storage.FileMode
import me.proxui.structure.chiccenAPI
import org.bukkit.entity.Player

private val map = chiccenAPI.database.getCollection("playerStyling", FileMode.DYNAMIC)

var Player.styling: PlayerStyling
    get() {
        return map.getOrSet(this.uniqueId.toString()) { PlayerStyling() }
    }
    set(value) {
        map[this.uniqueId.toString()] = value
    }

open class PlayerStyling {
    /**
     * 0-9 or a-f
     */
    var nameColor = 'f'

    /**
     * 0-9 or a-f
     */
    var messageColor = '7'

    var prefixes = mutableListOf<String>()
    var suffixes = mutableListOf<String>()

    fun getNameColor() = "§$nameColor"
    fun getMessageColor() = "§$messageColor"
    fun getPrefixesAsString() = prefixes.joinToString(" ").let { if (it.isBlank()) it else "$it §r" }

    fun getSuffixesAsString() = suffixes.joinToString(" ").let { if (it.isBlank()) it else " $it§r" }
}