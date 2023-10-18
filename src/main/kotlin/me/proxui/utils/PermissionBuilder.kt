package me.proxui.utils

import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

fun permission(builder: PermissionBuilder.() -> Unit) : Permission {
    PermissionBuilder().also {
        builder(it)
        return it.build()
    }
}

class PermissionBuilder {

    init {
        permission {
            name = ""
        }
    }

    var name: String = "placeholder"
    var default : PermissionDefault = PermissionDefault.FALSE
    var description: String? = null

    fun build() = Permission(this.name, this.description, this.default)
}