package me.proxui.structure

import me.proxui.dataholders.DataHolder
import me.proxui.dataholders.datafile.DataFile
import me.proxui.dataholders.database.impl.DataCollection
import me.proxui.player.PlayerDeafening
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit

val chiccenAPI by lazy { ChiccenAPI.INSTANCE }
val dataFile: DataHolder by lazy { DataFile(chiccenAPI, "data") }
val database: DataHolder by lazy { DataCollection("playerStyling") }
val remoteDataFile: DataHolder by lazy { DataCollection("data", "data") }

class ChiccenAPI : KSpigot() {

    /*
    TODO
        -fix dataFile generation
        -fix feature modules not taking configurations/resetting them - ???
        -fix command: players/-senders
        -fix proxy
        -add economy :) thats ez
        -add isDeaf tag
        -save playerStyling in database

        -lobby plugin
            -navigator
            -consistent saturation/health
            -bossbar advertisement/info
            -scoreboard: rank, friends online, online, coins, online-time
            -SilentArea
            -disable join msg

        -CustomChat
            -colors
            -slowmode + bypass
            -critical words + logging
            -emojis

         -add party plugin
            -@party/@p to write in party chat
         -add friends plugin
            -game invites


         -create gameMode api
     */

    companion object {
        internal lateinit var INSTANCE: ChiccenAPI
    }

    init {
        INSTANCE = this
    }

    override fun load() {
        logger.info("Loading plugin")
        Bukkit.getWorld("asd")
    }

    override fun startup() {
        PlayerDeafening()

        //load features
        ChiccenCommand.load()

        logger.info("Loaded plugin")
    }

    override fun shutdown() {
        logger.info("Shut down plugin")
    }
}