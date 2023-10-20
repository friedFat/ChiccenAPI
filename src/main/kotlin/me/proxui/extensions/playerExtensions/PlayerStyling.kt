package me.proxui.extensions.playerExtensions

import me.proxui.extensions.getObject
import me.proxui.structure.chiccenAPI
import org.bukkit.entity.Player

private val collection = chiccenAPI.database.getCollection("playerStyling")
private val stylingRegistry = collection.getObject<MutableMap<String, PlayerStyling>>("playerStyling") ?: mutableMapOf()
var Player.styling: PlayerStyling
    get() {
        collection.reload()
        return stylingRegistry.getOrPut(this.uniqueId.toString()) { PlayerStyling() }
    }
    set(value) {
        stylingRegistry[this.uniqueId.toString()] = value
        collection.save()
    }

open class PlayerStyling {
    var nameColor = 'f'
    var messageColor = '7'
    var prefixes = mutableListOf<String>()
    var suffixes = mutableListOf<String>()

    fun getPrefixesAsString() = prefixes.joinToString(" ").let { if (it.isBlank()) "" else "$it " }
    fun getSuffixesAsString() = suffixes.joinToString(" ").let { if (it.isBlank()) "" else " $it" }
}