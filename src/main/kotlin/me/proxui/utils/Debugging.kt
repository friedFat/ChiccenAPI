@file:Suppress("DEPRECATION")

package me.proxui.utils

import me.proxui.structure.database
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import java.util.logging.Logger

private val debugging = database.getCollection("debugging").getOrSet("debugging") { mutableSetOf<UUID>() }
var Player.isDebugging : Boolean
    get() = debugging.contains(this.uniqueId)
    set(value) {
        debugging.setContains(this.uniqueId, value)
    }

fun Logger.debug(msg: String) {
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
    logger.debug("Test#$id ---------------- " + if (passed) "passed" else "failed" +
            "\n>                                '$expected' | '$out'")
}