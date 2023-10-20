package me.proxui.storage

import org.bson.Document


abstract class Storage(document: Document) : Document(document) {
    abstract fun save()

    abstract fun reload()
}

/**
 * @throws ClassCastException if the type for key is not [T]
 */
fun <T> Document.getOrSet(key: String, altValue: T): T {
    if (!this.containsKey(key)) {
        this[key] = altValue
        return altValue
    }
    @Suppress("UNCHECKED_CAST")
    return this[key] as T
}