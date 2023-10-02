package me.proxui.extensions.playerExtensions

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import me.proxui.structure.chiccenAPI
import me.proxui.utils.setContains
import org.bukkit.entity.Player
import java.util.*


private val deafPlayers = mutableSetOf<UUID>()
var Player.isDeaf: Boolean
    get() = deafPlayers.contains(uniqueId)
    set(value) {
        deafPlayers.setContains(this.uniqueId, value)
    }

object PlayerDeafening {
    init {
        val manager = ProtocolLibrary.getProtocolManager()
        manager.addPacketListener(object : PacketAdapter(chiccenAPI, ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT, PacketType.Play.Server.ENTITY_SOUND) {
            override fun onPacketSending(e: PacketEvent) {
                if (e.player.isDeaf) e.isCancelled = true
            }
        })
    }
}