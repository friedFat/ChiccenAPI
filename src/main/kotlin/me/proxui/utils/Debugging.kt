package me.proxui.utils

import me.proxui.structure.chiccenAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import java.util.logging.Logger

private val collection = chiccenAPI.database.getCollection("debugging")
private val debugging : ArrayList<UUID>
    get() {
        collection.reload()
        return collection.getOrSet("uuids", arrayListOf())
    }

var Player.isDebugging: Boolean
    get() = debugging.contains(this.uniqueId)
    set(value) {
        debugging.setContains(this.uniqueId, value)
        collection.save()
    }

fun Logger.debug(message: String) {
    val caller = Thread.currentThread().stackTrace[3]
    val msg = "${caller.className}:${caller.lineNumber} -> $message"

    @Suppress("DEPRECATION")
    this.info(ChatColor.stripColor(msg))
    debugging.forEach {
        (Bukkit.getPlayer(it) ?: return@forEach).sendMessage("§8§l[Debug]§r§7 $msg")
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