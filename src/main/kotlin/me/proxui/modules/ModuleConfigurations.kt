package me.proxui.modules

import me.proxui.storage.Storage

@Target(AnnotationTarget.FIELD)
annotation class ConfigOption(val name: String)

object ConfigOptionManager {

    fun applyConfigurations(instance: Module<*>, config: Storage) {
        val clazz = instance::class.java
        getOptions(instance).forEach { ids ->
            val field = clazz.getField(ids.fieldName)
            field.isAccessible = true
            field.set(instance, config.getOrSet(instance.name + ids.configKey) {
                getDefault(instance, ids)
            })
        }
    }

    private fun getOptions(instance: Module<*>): Set<ModuleIdentifiers> {
        val clazz = instance::class.java

        val configurableFields = clazz.fields.filter {
            it.isAnnotationPresent(ConfigOption::class.java)
        }

        val result = mutableSetOf<ModuleIdentifiers>()
        configurableFields.forEach {
            val configKey = it.getAnnotation(ConfigOption::class.java).name
            val fieldName = it.name
            result.add(ModuleIdentifiers(configKey, fieldName))
        }
        return result
    }

    private fun getDefault(instance: Module<*>, ids: ModuleIdentifiers): Any = instance::class.java.getField(ids.fieldName).get(instance)

    data class ModuleIdentifiers(val configKey: String, val fieldName: String)
}