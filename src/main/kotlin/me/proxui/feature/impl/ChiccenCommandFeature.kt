package me.proxui.feature.impl

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import me.proxui.storage.datafile.DataFile
import me.proxui.extensions.playerExtensions.balance
import me.proxui.feature.Feature
import me.proxui.feature.FeatureInfo
import me.proxui.structure.chiccenAPI
import me.proxui.utils.*
import net.axay.kspigot.commands.*
import net.minecraft.commands.arguments.EntityArgument

@FeatureInfo("ChiccenCommand")
object ChiccenCommandFeature : Feature {
    override fun onLoad() {
        command("chiccen") {
            requiresPermission(pluginPermission(chiccenAPI, "manage"))

            literal("saveFiles") {
                runs {
                    DataFile.REGISTERED_DATA_FILES.forEach { file ->
                        file.save()
                    }
                    this.player.sendMessage("Saved all config/data files")
                }
            }
            literal("reloadFiles") {
                runs {
                    DataFile.REGISTERED_DATA_FILES.forEach { file ->
                        file.reload()
                    }
                    this.player.sendMessage("Reload all config/data files")
                }
            }
            literal("debug") {
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
                        p.isDeaf = true
                        p.sendMessage("You are now deaf")
                    }
                }
                literal("eco") {
                    argument("player", EntityArgument.player()) {
                        literal("get") {
                            runs {
                                val target = EntityArgument.getPlayer(this.nmsContext, "player").bukkitEntity.player!!
                                p.sendMessage("${target.name} has ${target.balance} coins")
                            }
                        }
                        literal("set") {
                            argument("amount", IntegerArgumentType.integer()) {
                                runs {
                                    val target = EntityArgument.getPlayer(this.nmsContext, "player").bukkitEntity.player!!
                                    val amount = IntegerArgumentType.getInteger(this.nmsContext, "amount")
                                    target.balance = amount
                                    p.sendMessage("Set ${target.name} balance to $amount coins")
                                }
                            }
                        }
                        literal("add") {
                            argument("amount", IntegerArgumentType.integer()) {
                                runs {
                                    val target = EntityArgument.getPlayer(this.nmsContext, "player").bukkitEntity.player!!
                                    val amount = IntegerArgumentType.getInteger(this.nmsContext, "amount")
                                    target.balance += amount
                                    p.sendMessage("Gave ${target.name} $amount coins")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}