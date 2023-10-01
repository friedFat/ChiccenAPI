package me.proxui.utils

import me.proxui.structure.chiccenAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import java.util.logging.Logger

private val collection = chiccenAPI.database.getCollection("debugging")
private val debugging = mutableSetOf<UUID>()

var Player.isDebugging: Boolean
    get() {
        collection.reload()
        return collection[this.uniqueId.toString()] ?: false
    }
    set(value) {
        collection[this.uniqueId.toString()] = if(value) true else null
        collection.save()

        debugging.setContains(this.uniqueId, value)
    }

fun Logger.debug(msg: String) {
    @Suppress("DEPRECATION")
    this.info(ChatColor.stripColor(msg))
    debugging.forEach {
        (Bukkit.getPlayer(it) ?: return@forEach).sendMessage("§8Debug -> $msg")
    }
}

/**
 * @param id the id of the test
 * @param expected the expected output
 * @param output the action that should return [expected]
 */
fun test(id: Int, expected: String, output: () -> String) {
    val out = output()
    val passed = expected == out
    logger.debug(
        "Test#$id ---------------- " + if (passed) "passed" else "failed" +
                "\n>                                '$expected' | '$out'"
    )
}