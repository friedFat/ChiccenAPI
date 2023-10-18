package me.proxui.events

import me.proxui.extensions.playerExtensions.balance
import org.bukkit.OfflinePlayer
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerBalanceUpdateEvent(val player: OfflinePlayer, newBalance: Int) : Event(), Cancellable {

    val previousBalance = player.balance
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
