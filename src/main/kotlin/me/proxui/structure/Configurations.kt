package me.proxui.structure

import org.bukkit.plugin.java.JavaPlugin

interface Configurations {
    val useDatabase: Boolean
    val plugin: JavaPlugin
    val databaseURL: String
}