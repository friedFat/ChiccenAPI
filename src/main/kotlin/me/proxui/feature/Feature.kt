package me.proxui.feature

import me.proxui.storage.datafile.DataFile
import me.proxui.structure.chiccenAPI
import me.proxui.utils.setContains

/**
 * @param name The name the feature should have
 * @param enabledByDefault whether it should be enabled by default or not. This is true by default
 */
@Target(AnnotationTarget.CLASS)
annotation class FeatureInfo(val name: String, val enabledByDefault: Boolean = true)

interface Feature {

    companion object {
        private val featureConfig by lazy { DataFile(chiccenAPI, "features") }
        private val loadedFeatures by lazy { mutableSetOf<Feature>() }
    }

    private val featureInfo: FeatureInfo?; get() = this::class.java.getAnnotation(FeatureInfo::class.java)
    val name; get() = featureInfo?.name ?: this::class.simpleName ?: this.toString()
    val enabledByDefault; get() = featureInfo?.enabledByDefault ?: true
    var isLoaded; get() = loadedFeatures.contains(this)
        set(value) = loadedFeatures.setContains(this, value)


    fun load() {
        //avoid multiple initialisations
        if(isLoaded) throw IllegalStateException("Feature is already loaded and can not be loaded twice")
        isLoaded = true

        val shouldLoad = featureConfig.getOrSet("$name.isEnabled", enabledByDefault)
        if(shouldLoad) onLoad()
    }

    /**
     * This function will be called after enabling the feature
     */
    fun onLoad()
}