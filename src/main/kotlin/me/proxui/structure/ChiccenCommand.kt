package me.proxui.structure

import com.mojang.brigadier.arguments.BoolArgumentType
import me.proxui.dataholders.datafile.DataFile
import me.proxui.feature.Feature
import me.proxui.feature.FeatureInfo
import me.proxui.utils.*
import net.axay.kspigot.commands.*

@FeatureInfo("ChiccenCommand")
object ChiccenCommand : Feature {

    override fun onEnable() {
        command("chiccen") {
            requiresPermission(pluginPermission(chiccenAPI, "coreCommand"))

            literal("saveFiles") {
                runs {
                    DataFile.REGISTERED_DATA_FILES.forEach { file ->
                        file.save()
                        file.reload()
                    }
                    this.player.sendMessage("Reloaded all config/data files")
                }
            }

            literal("debugging") {
                argument("state", BoolArgumentType.bool()) {
                    runs {
                        val newState = getArgument("state", Boolean::class)
                        p.isDebugging = newState
                        p.sendMessage("Debugging state has been updated")
                    }
                }
            }

            literal("test") {
                literal("deafening") {
                    runs {
                        player.isDeaf = true
                        player.sendMessage("You are now deaf")
                    }
                }
            }
        }
    }
}