package me.proxui.extensions

import net.axay.kspigot.commands.CommandContext

fun <T> MutableSet<T>.setContains(element: T, contains: Boolean) {
    if (contains) add(element)
    else remove(element)
}

fun <T> MutableList<T>.setContains(element: T, boolean: Boolean) {
    if (boolean) this.add(element)
    else this.remove(element)
}

/**
 * Same as CommandContext#player
 */
val CommandContext.p; get() = player