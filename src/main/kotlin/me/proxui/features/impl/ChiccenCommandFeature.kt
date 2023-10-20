package me.proxui.features.impl

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import me.proxui.extensions.p
import me.proxui.extensions.playerExtensions.balance
import me.proxui.features.Feature
import me.proxui.structure.chiccenAPI
import me.proxui.utils.isDebugging
import net.axay.kspigot.commands.*
import net.minecraft.commands.arguments.EntityArgument

object ChiccenCommandFeature : Feature(chiccenAPI,"ChiccenCommand") {
    override fun onInitialization() {
        command("chiccen") {

            runs {
                p.sendMessage("Put put!")
            }

            literal("debug") {
                requiresPermission("chiccenapi.command.chiccen.debug")
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
                requiresPermission("chiccenapi.command.chiccen.test")

                literal("database") {
                    literal("set") {
                        argument("value", IntegerArgumentType.integer()) {
                            argument("save", BoolArgumentType.bool()) {
                                runs {
                                    chiccenAPI.database.getCollection("bitches").also {
                                        it["amount"] = IntegerArgumentType.getInteger(this.nmsContext, "value")
                                        if (BoolArgumentType.getBool(this.nmsContext, "save")) {
                                            it.save()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    literal("getValue") {
                        runs {
                            argument("reload", BoolArgumentType.bool())
                            chiccenAPI.database.getCollection("bitches").also {
                                if (BoolArgumentType.getBool(this.nmsContext, "reload")) {
                                    it.reload()
                                }
                                p.sendMessage("Amount of bitches: " + it["amount"])
                            }
                        }
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