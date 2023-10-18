package me.proxui.structure

import org.bukkit.plugin.Plugin

interface Configurations {
    val saveLocally: Boolean
    val plugin: Plugin
    val databaseURL: String
}