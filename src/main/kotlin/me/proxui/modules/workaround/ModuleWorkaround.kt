package me.proxui.modules.workaround

import me.proxui.modules.EmptyModuleConfigurations
import me.proxui.modules.Module
import org.bukkit.plugin.Plugin

/**
 * Workaround, because default values for generics r not possible? IDK but this should work
 */
abstract class Module(plugin: Plugin, name: String, enabledByDefault: Boolean = true) : Module<EmptyModuleConfigurations>(plugin, name, enabledByDefault, EmptyModuleConfigurations)