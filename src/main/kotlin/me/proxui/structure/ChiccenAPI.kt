package me.proxui.structure

import me.proxui._storage.Storage
import me.proxui._storage.database.mongo.IMongoDatabase
import me.proxui._storage.database.mongo.MongoDatabase
import me.proxui._storage.datafile.DataFile
import me.proxui._storage.getOrSet
import me.proxui.extensions.playerExtensions.PlayerDeafening
import me.proxui.features.impl.ChiccenCommandFeature
import me.proxui.features.impl.SafeReloadFeature
import net.axay.kspigot.main.KSpigot

val chiccenAPI by lazy { ChiccenAPI.INSTANCE }

class ChiccenAPI : KSpigot(), Configurations {

    override lateinit var databaseURL: String; private set
    val database: IMongoDatabase by lazy { MongoDatabase(chiccenAPI, "minecraft") }
    override val saveLocally: Boolean = false
    override val plugin; get() = this

    private val configFile: Storage by lazy { DataFile(this, "configs") }

    override fun load() {
        INSTANCE = this
        logger.info("Loading plugin...")
    }

    override fun startup() {
        databaseURL = configFile.getOrSet("databaseURL", "mongodb+srv://username:<password>@<host>:<port>?")


        //load features
        PlayerDeafening.init()
        ChiccenCommandFeature.init()
        SafeReloadFeature.init()

        logger.info("Loaded plugin!")
    }

    override fun shutdown() {
        logger.info("Shut down plugin!")
    }

    companion object {
        internal lateinit var INSTANCE: ChiccenAPI
        /*
        TODO
            -test debugging tracker - not working
            -test storage system: serialization might not be working

            -configure "NetWorld"

            core plugin:
                -styling command to manage a players styling
                -chat:
                    -colors
                    -slow-mode + bypass
                    -critical message logging
                    -:hand:, if enabled in server (ChiccenCommand to set)

                -party plugin
                    -@party/@p to write in party chat
                    -"/pc" or "/partyChat" to switch to party chat or to write for once
                    -"/all" to write in public chat

                friends plugin
                    -game invites
                    -friend list
                    -online friends amount

            lobby plugin
                -navigator
                -consistent saturation/health
                -gm 2
                -bossbar advertisement/info
                -scoreboard: rank, friends online, online, coins, online-time
                -SilentArea + /pWeather + setting to mute weather
                -disable join msg


             mini game API - im scared of this
                -phases
                -WorldCreator - maybe steal from noRisk :>, https://github.com/copyandexecute/youtuber-ideen-modus/blob/main/src/main/kotlin/de/hglabor/youtuberideen/game/phases/LobbyPhase.kt#L23 this good

             vanillaTweaks
                -gamemode 0 + gm alias
                -vanish
                -inv see
                -ec see
                -mute
                -warn
                -better ban/kick screen
                -unban alias for pardon
         */
    }
}