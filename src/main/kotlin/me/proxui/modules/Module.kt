package me.proxui.modules

import me.proxui.extensions.setContains
import me.proxui.storage.FileMode
import me.proxui.storage.datafile.Datafile
import me.proxui.structure.chiccenAPI
import org.bukkit.plugin.Plugin
import kotlin.reflect.KClass

object EmptyModuleConfigurations : ModuleConfiguration

abstract class Module<T: ModuleConfiguration>(private val plugin: Plugin, val name: String, val enabledByDefault: Boolean = true, configurations: T) {

    var configurations: T = configurations; private set
    var isInitialized
        private set(value) = initializedFeatures.setContains(this::class, value)
        get() = initializedFeatures.contains(this::class)

    private val defaultData; get() = ModuleData(enabledByDefault, configurations)

    init {
        ConfigOptionManager.applyConfigurations(this, featureConfigFile)
    }

    fun init() {
        val id = "${plugin.name}_${this.name}"
        check(!isInitialized) {
            "Feature '$id' is already initialized and can not be initialized twice!"
        }

        val data = featureConfigFile.getOrSet(id, defaultData)
        this.configurations = data.configurations

        if (data.isEnabled) {
            isInitialized = true
            onInitialization()
        }
    }

    /**
     * Function that will be called when initializing the feature
     */
    abstract fun onInitialization()

    companion object {
        private val initializedFeatures = mutableSetOf<KClass<out Module<*>>>()
        private val featureConfigFile by lazy { Datafile(chiccenAPI, "features", fileMode = FileMode.DYNAMIC) }
    }
}

data class ModuleData<T : ModuleConfiguration>(val isEnabled: Boolean, val configurations: T)

interface ModuleConfiguration