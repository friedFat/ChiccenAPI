package me.proxui.utils

import me.proxui.extensions.setContains
import me.proxui.storage.FileMode
import me.proxui.structure.chiccenAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import java.util.logging.Logger

private val collection = chiccenAPI.database.getCollection("debugging", FileMode.SAVE_ON_STOP)

val debuggingPlayers: MutableSet<UUID>; get() = collection["uuids"] ?: mutableSetOf()

var Player.isDebugging: Boolean
    get() {
        collection.reload()
        return debuggingPlayers.contains(this.uniqueId)
    }
    set(value) {
        debuggingPlayers.setContains(this.uniqueId, value)
        collection.save()
    }

fun Logger.debug(message: String) {
    val caller = Thread.currentThread().stackTrace[3]
    val msg = "${caller.className}:${caller.lineNumber} -> $message"

    @Suppress("DEPRECATION")
    this.info(ChatColor.stripColor(msg))
    debuggingPlayers.forEach {
        (Bukkit.getPlayer(it) ?: return@forEach).sendMessage("§8§l[Debug]§r§7 $msg")
    }
}

/**
 * @param id the id of the test
 * @param expected the expected output
 * @param output the action that returns [expected].. or not
 */
fun <T> test(id: Int, expected: T, output: () -> T) {
    val out = output()
    val passed = expected == out
    logger.debug(
        "Test#$id ---------------- " + if (passed) "passed" else "failed" +
                "\n>                                '$expected' | '$out'"
    )
}