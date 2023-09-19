package me.proxui.structure

import me.proxui.dataholders.DataHolder
import me.proxui.dataholders.database.DatabaseConfigurator
import me.proxui.dataholders.database.IDatabase
import me.proxui.dataholders.database.impl.Database
import me.proxui.dataholders.datafile.DataFile
import me.proxui.player.PlayerDeafening
import net.axay.kspigot.main.KSpigot

val chiccenAPI by lazy { ChiccenAPI.INSTANCE }
val dataFile: DataHolder by lazy { DataFile(chiccenAPI, "data") }
val configFile: DataHolder by lazy { DataFile(chiccenAPI, "configs") }
val database: IDatabase by lazy { Database(chiccenAPI.dbConfigs,"data") }

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

    /**
     * database configurations
     */
    val dbConfigs by lazy {
        object: DatabaseConfigurator {
            override val host by lazy { configFile.getOrSet("host", "host-ip") }
            override val port by lazy { configFile.getOrSet("port", 27017) }
        }
    }


    init {
        INSTANCE = this
    }

    override fun load() {
        logger.info("Loading plugin")
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