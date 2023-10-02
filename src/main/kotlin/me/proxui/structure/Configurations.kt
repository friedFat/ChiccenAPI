package me.proxui.structure

import me.proxui.storage.database.DatabaseConfiguration
import org.bukkit.plugin.java.JavaPlugin

interface Configurations {
    val inDev: Boolean
    val plugin: JavaPlugin
    val databaseConfiguration: DatabaseConfiguration
}