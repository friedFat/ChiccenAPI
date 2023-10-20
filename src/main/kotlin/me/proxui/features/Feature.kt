package me.proxui.features

import me.proxui.extensions.setContains
import me.proxui.storage.datafile.Datafile
import me.proxui.storage.getOrSet
import me.proxui.structure.chiccenAPI
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

abstract class Feature(private val plugin: Plugin, val name: String, val enabledByDefault: Boolean = true) {

    var isInitialized
        private set(value) {
            initializedFeatures.setContains(this::class, value)
        }
        get() = initializedFeatures.contains(this::class)

    fun init() {
        val shouldLoad = featureConfigFile.getOrSet("${plugin.name}_${this.name}.isEnabled", enabledByDefault)

        check(!isInitialized) {
            "Feature '${plugin.name}_${this.name}' is already initialized and can not be initialized twice!"
        }

        if (shouldLoad) {
            onInitialization()
            isInitialized = true
        }
    }

    /**
     * Function that will be called when initializing the feature
     */
    abstract fun onInitialization()

    companion object {
        private val initializedFeatures = mutableSetOf<KClass<out Feature>>()
        private val featureConfigFile by lazy { Datafile(chiccenAPI, "features") }
    }
}