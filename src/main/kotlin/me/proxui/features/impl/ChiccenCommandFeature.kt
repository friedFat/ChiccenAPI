package me.proxui.features.impl

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import me.proxui.extensions.playerExtensions.balance
import me.proxui.features.Feature
import me.proxui.storage.SavableStorage
import me.proxui.structure.chiccenAPI
import me.proxui.utils.isDebugging
import me.proxui.utils.p
import me.proxui.utils.pluginPermission
import me.proxui.utils.withEach
import net.axay.kspigot.commands.*
import net.minecraft.commands.arguments.EntityArgument

object ChiccenCommandFeature : Feature("ChiccenCommand") {
    override fun onInitialization() {
        command("chiccen") {
            requiresPermission(pluginPermission(chiccenAPI, "command.chiccen"))

            literal("ip") {
                requiresPermission(pluginPermission(chiccenAPI, "command.chiccen.ip"))
                runs {
                    p.sendMessage("IP of the server: ${server.ip}")
                }
            }

            literal("saveData") {
                requiresPermission(pluginPermission(chiccenAPI, "command.chiccen.saveData"))
                runs {
                    try {
                        SavableStorage.getRegistry().withEach { save() }
                        p.sendMessage("Saved all data files")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }

            literal("reloadData") {
                requiresPermission(pluginPermission(chiccenAPI, "command.chiccen.reloadData"))
                runs {
                    try {
                        SavableStorage.getRegistry().withEach { reload() }
                        p.sendMessage("Reload all data files")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }

            literal("debug") {
                requiresPermission(pluginPermission(chiccenAPI, "command.chiccen.debug"))
                argument("state", BoolArgumentType.bool()) {
                    runs {
                        try {
                            val newState = BoolArgumentType.getBool(nmsContext, "state")
                            p.isDebugging = newState
                            p.sendMessage("Debugging state has been updated")
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
                runs {
                    try {
                        p.isDebugging
                        p.sendMessage("You are currently " + (if (p.isDebugging) "" else "not ") + "debugging")
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }

            literal("test") {
                requiresPermission(pluginPermission(chiccenAPI, "command.chiccen.test"))

                literal("eco") {
                    requiresPermission(pluginPermission(chiccenAPI, "command.chiccen.test.eco"))
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
                                    try {
                                        val target = EntityArgument.getPlayer(this.nmsContext, "player").bukkitEntity.player!!
                                        val amount = IntegerArgumentType.getInteger(this.nmsContext, "amount")
                                        target.balance = amount
                                        p.sendMessage("Set the balance of ${target.name} to $amount coins")
                                    }catch (ex: Exception) {
                                        ex.printStackTrace()
                                    }
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