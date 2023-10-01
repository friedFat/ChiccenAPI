package me.proxui.events

import org.bukkit.OfflinePlayer
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerBalanceUpdateEvent(val player: OfflinePlayer, val previousBalance: Int, newBalance: Int) : Event(), Cancellable {

    private var isCancelled = false
    var newBalance = newBalance
        set(value) {
            field = value
            added = field - previousBalance
        }

    /**
     * This can also be negative if money was taken
     */
    var added = 0
        get() = newBalance - previousBalance
        set(value) {
            field = value
            newBalance = previousBalance + field
        }

    override fun isCancelled() = isCancelled
    override fun setCancelled(cancel: Boolean) {
        isCancelled = cancel
    }

    override fun getHandlers() = HandlerList()
}
