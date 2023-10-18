package me.proxui.utils

import java.util.*
import kotlin.math.ceil


data class Cooldown(val defaultMillisLength: Int) {

    private val expiringTime = HashMap<UUID, Long>()

    fun setCooldown(uuid: UUID, millisLength: Int = defaultMillisLength) {
        expiringTime[uuid] = System.currentTimeMillis() + millisLength
    }

    fun hasCooldown(uuid: UUID): Boolean {
        return getMillisLeft(uuid) > 0
    }

    fun getMillisLeft(uuid: UUID): Int {
        return (expiringTime.getOrDefault(uuid, 0L) - System.currentTimeMillis()).toInt()
    }

    fun getSecondsLeft(uuid: UUID): Int {
        return ceil(getMillisLeft(uuid).toDouble()).toInt()
    }
}