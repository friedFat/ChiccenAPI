package me.proxui.extensions

import com.google.gson.Gson
import org.bson.Document

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

inline fun <reified T> Document.getObject(key: String) : T? {
    return Gson().fromJson(this.getString(key), T::class.java)
}