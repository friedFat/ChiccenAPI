package me.proxui.features

import me.proxui._storage.datafile.DataFile
import me.proxui._storage.getOrSet
import me.proxui.structure.chiccenAPI
import me.proxui.utils.setContains
import kotlin.reflect.KClass

abstract class Feature(val name: String, val enabledByDefault: Boolean = true) {

    var isInitialized
        private set(value) {
            initializedFeatures.setContains(this::class, value)
        }
        get() = initializedFeatures.contains(this::class)

    fun init() {
        val shouldLoad = featureConfig.getOrSet("$name.isEnabled", enabledByDefault)

        check(!isInitialized) {
            "Feature '$name' is already initialized and can not be initialized twice!"
        }

        if (initializedFeatures.contains(this::class))

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
        private val featureConfig by lazy { DataFile(chiccenAPI, "features") }
    }
}