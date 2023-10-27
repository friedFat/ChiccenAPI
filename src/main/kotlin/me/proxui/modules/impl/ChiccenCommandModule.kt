package me.proxui.modules.impl

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import me.proxui.extensions.p
import me.proxui.extensions.playerExtensions.balance
import me.proxui.modules.workaround.Module
import me.proxui.structure.chiccenAPI
import me.proxui.utils.isDebugging
import net.axay.kspigot.commands.*
import net.minecraft.commands.arguments.EntityArgument

object ChiccenCommandModule : Module(chiccenAPI, "ChiccenCommand") {
    override fun onInitialization() {
        command("chiccen") {

            runs {
                p.sendMessage("Put put!")
            }

            literal("debug") {
                requiresPermission("chiccenapi.command.chiccen.debug")
                argument("state", BoolArgumentType.bool()) {
                    runs {
                        val newState = BoolArgumentType.getBool(nmsContext, "state")
                        p.isDebugging = newState
                        p.sendMessage("Debugging state has been updated")
                    }
                }
                runs {
                    p.sendMessage("Debugging mode is currently " + (if (p.isDebugging) "" else "not ") + "enabled for you")
                }
            }

            literal("test") {
                requiresPermission("chiccenapi.command.chiccen.test")

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
                                    try {
                                        val target = EntityArgument.getPlayer(this.nmsContext, "player").bukkitEntity.player!!
                                        val amount = IntegerArgumentType.getInteger(this.nmsContext, "amount")
                                        target.balance = amount
                                        p.sendMessage("Set the balance of ${target.name} to $amount coins")
                                    } catch (ex: Exception) {
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