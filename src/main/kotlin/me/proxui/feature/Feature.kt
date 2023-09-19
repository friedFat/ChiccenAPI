package me.proxui.feature

import me.proxui.structure.Values

/**
 * @param name The name the feature should have
 * @param enabledByDefault whether it should be enabled by default or not. This is true by default
 */
@Target(AnnotationTarget.CLASS)
annotation class FeatureInfo(val name: String, val enabledByDefault: Boolean = true)

interface Feature {

    /**
     * This function will be called after enabling the feature
     */
    fun onEnable()

    companion object {
        private val wasLoaded = mutableListOf<Feature>()
    }

    private val featureInfo: FeatureInfo?; get() = this::class.java.getAnnotation(FeatureInfo::class.java)
    val name; get() = featureInfo?.name ?: this::class.simpleName ?: this.toString()
    val enabledByDefault; get() = featureInfo?.enabledByDefault ?: true

    fun load() {
        //avoid multiple initialisations
        if(wasLoaded.contains(this)) throw UnsupportedOperationException("Feature can not be loaded twice")
        wasLoaded.add(this)

        val isEnabled = Values.featureConfig.getOrSet("$name.isEnabled", enabledByDefault)
        if(isEnabled) onEnable()
    }
}