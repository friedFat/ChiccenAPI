package me.proxui._storage

import org.bson.Document


abstract class Storage(document: Document) : Document(document) {
    abstract fun save()

    abstract fun reload()
}

fun <T> Document.setAndGet(key: String, value: T): T {
    this[key] = value
    return value
}

/**
 * @throws ClassCastException if the type for key is not [T]
 */
fun <T> Document.getOrSet(key: String, altValue: () -> T): T {
    if (!this.containsKey(key)) {
        val element = altValue()
        this[key] = element
        return element
    }
    @Suppress("UNCHECKED_CAST")
    return this[key] as T
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