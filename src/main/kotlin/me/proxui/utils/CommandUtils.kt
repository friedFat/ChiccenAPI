package me.proxui.utils

import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import org.bukkit.entity.Player

class UnsupportedSenderException(msg: String = "") : UnsupportedOperationException(msg) {
    override fun getStackTrace(): Array<StackTraceElement> {
        return arrayOf()
    }
}

fun CommandContext<CommandSourceStack>.tryGetPlayer(): Player {
    if(this.source.player == null){
        throw NullPointerException("player null")
    }
    if(this.source.player!!.bukkitEntity.player == null){
        throw NullPointerException("entity.player null")
    }
    val p = this.source.player?.bukkitEntity?.player ?: throw UnsupportedSenderException("Only players are allowed to use this command")
    return p
}