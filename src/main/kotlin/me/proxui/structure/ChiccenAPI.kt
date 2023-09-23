package me.proxui.structure

import me.proxui.feature.impl.ChiccenCommandFeature
import me.proxui.storage.Storage
import me.proxui.storage.database.DatabaseConfiguration
import me.proxui.storage.database.IDatabase
import me.proxui.storage.database.impl.Database
import me.proxui.storage.datafile.DataFile
import me.proxui.utils.PlayerDeafening
import net.axay.kspigot.main.KSpigot

val chiccenAPI by lazy { ChiccenAPI.INSTANCE }
val database: IDatabase by lazy { Database(chiccenAPI, "data") }

class ChiccenAPI : KSpigot(), Configurations {

    private val configFile: Storage by lazy { DataFile(chiccenAPI, "configs") }
    override val inDev: Boolean = true
    override val plugin; get() = this
    override val databaseConfiguration by lazy {
        DatabaseConfiguration(
            configFile.getOrSet("host", "host"),
            configFile.getOrSet("port", 27017),
            configFile.getOrSet("username", "username"),
            configFile.getOrSet("password", "password")
        )
    }

    companion object {
        /*
        TODO
            -i think retrieving data from file after reloading doesnt work - probably linked with resetting
            -fix feature modules resetting configurations - made it use containsKey but did not test

            -fix command entity argument
            -database
            -make debugging store in database

            -isDeaf not silencing everything
            -fix proxy
            -add economy :) thats ez - should work now but unused
            -WorldCreator - maybe steal from noRisk :>, https://github.com/copyandexecute/youtuber-ideen-modus/blob/main/src/main/kotlin/de/hglabor/youtuberideen/game/phases/LobbyPhase.kt#L23 this good

            -lobby plugin
                -navigator
                -consistent saturation/health
                -bossbar advertisement/info
                -scoreboard: rank, friends online, online, coins, online-time
                -SilentArea + /pWeather + setting to mute weather
                -disable join msg

                -CustomChat
                -colors
                -slowmode + bypass
                -critical words + logging
                -:hand: if not disabled(chiccenAPI#chatOptions#allowFlexxing

             -add party plugin
                -@party/@p to write in party chat
                -/pc or partyChat to toggle

             -add friends plugin
                -game invites

             -create minigame API
             -vanillaTweaks :)
         */
        internal lateinit var INSTANCE: ChiccenAPI
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
        ChiccenCommandFeature.load()

        logger.info("Loaded plugin")
    }

    override fun shutdown() {
        logger.info("Shut down plugin")
    }
}