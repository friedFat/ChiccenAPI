package me.proxui.structure

import com.mojang.brigadier.arguments.BoolArgumentType
import me.proxui.dataholders.datafile.DataFile
import me.proxui.feature.Feature
import me.proxui.feature.FeatureInfo
import me.proxui.player.isDeaf
import me.proxui.utils.*
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.literal
import net.axay.kspigot.commands.requiresPermission
import net.minecraft.commands.arguments.EntityArgument
import org.bukkit.entity.Player

@FeatureInfo("ChiccenCommand")
object ChiccenCommand : Feature {

    override fun onEnable() {
        command("chiccen") {
            requiresPermission(pluginPermission(chiccenAPI, "coreCommand"))

            literal("reloadConfig") {
                executes {
                    DataFile.REGISTERED_DATA_FILES.forEach { file ->
                        file.reload()
                    }
                    it.source.bukkitSender.sendMessage("Reloaded all config/data files")
                    0
                }
            }

            literal("debugging") {
                argument("state", BoolArgumentType.bool()) {
                    executes {/*
                        logger.debug("executes")
                        val p = it.source.player?.bukkitEntity?.player ?: throw
                        val newState = it.getArgument("state", Boolean::class.java)
                        p.isDebugging = newState
                        p.sendMessage("Updated debugging state")*/
                        return@executes 0
                    }
                }
            }

            literal("saveAllFiles") {
                executes { cmd ->
                    DataFile.REGISTERED_DATA_FILES.forEach {
                        it.save()
                    }
                    cmd.tryGetPlayer().sendMessage("Saved ${DataFile.REGISTERED_DATA_FILES.size} data-files")
                    return@executes 0
                }
            }

            literal("runTest") {
                literal("tryGetPlayer") {
                    executes {
                        logger.debug("running tests")
                        it.source.player?.bukkitEntity?.player?.sendMessage("1")
                        it.source.bukkitSender.sendMessage("2")
                        try {
                            (it.source.bukkitEntity as Player).sendMessage("3")
                        }catch (_: ClassCastException) {
                            throw UnsupportedSenderException("source.bukkitEntity is not a player!")
                        }
                        logger.debug("source.playerOrException: "+it.source.playerOrException.name)
                        logger.debug("ended tests")
                        return@executes 0
                    }
                }
                literal("makeMeDeaf") {
                    argument("player", EntityArgument.player())
                    executes {
                        EntityArgument.getPlayer(it, "player").bukkitEntity.player!!.isDeaf = true
                        0
                    }
                }
            }
        }
    }
}