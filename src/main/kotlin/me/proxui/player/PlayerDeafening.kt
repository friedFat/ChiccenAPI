package me.proxui.player

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import me.proxui.structure.chiccenAPI
import org.bukkit.entity.Player
import java.util.*


private val deafPlayers = mutableListOf<UUID>()
var Player.isDeaf: Boolean
    get() = deafPlayers.contains(this.uniqueId)
    set(value) {
        if(value) deafPlayers.add(this.uniqueId)
        else deafPlayers.remove(this.uniqueId)
    }

class PlayerDeafening {
    init {
        val manager = ProtocolLibrary.getProtocolManager()
        manager.addPacketListener(object: PacketAdapter(chiccenAPI, ListenerPriority.NORMAL, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            override fun onPacketSending(e: com.comphenix.protocol.events.PacketEvent) {
                if (e.player.isDeaf) e.isCancelled = true
            }
        })
    }
}