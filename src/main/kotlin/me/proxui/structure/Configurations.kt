package me.proxui.structure

import org.bukkit.plugin.java.JavaPlugin

interface Configurations {
    val inDev: Boolean
    val plugin: JavaPlugin
    val databaseURL: String
}