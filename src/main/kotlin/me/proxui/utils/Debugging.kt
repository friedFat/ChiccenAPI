package me.proxui.utils

import me.proxui.structure.dataFile
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import java.util.logging.Logger

private val debugging = dataFile.getOrSet("debugging") { mutableListOf<UUID>() }
var Player.isDebugging : Boolean
    get() = debugging.contains(this.uniqueId)
    set(value) {
        if(value) debugging.add(this.uniqueId)
        else debugging.remove(this.uniqueId)
    }

fun Logger.debug(msg: String) {
    @Suppress("DEPRECATION")
    this.info(ChatColor.stripColor(msg))
    debugging.forEach {
        (Bukkit.getPlayer(it) ?: return@forEach).sendMessage("§8Debug -> $msg")
    }
}

/**
 * @param id the id
 * @param expected the expected output
 * @param output the output
 */
fun test(id: Int, expected: String, output: () -> String) {
    val out = output()
    val passed = expected == out
    logger.debug("Test#$id ---------------- " + if (passed) "passed" else "failed" +
            "\n>                                '$expected' | '$out'")
}