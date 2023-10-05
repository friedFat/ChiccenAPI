package me.proxui.structure

import me.proxui.extensions.playerExtensions.PlayerDeafening
import me.proxui.feature.impl.ChiccenCommandFeature
import me.proxui.storage.Savable
import me.proxui.storage.database.IDatabase
import me.proxui.storage.database.Database
import me.proxui.storage.datafile.DataFile
import net.axay.kspigot.main.KSpigot

val chiccenAPI by lazy { ChiccenAPI.INSTANCE }

class ChiccenAPI : KSpigot(), Configurations {

    val database: IDatabase by lazy { Database(chiccenAPI, "minecraft") }
    override val useDatabase: Boolean = false
    override val plugin; get() = this
    private val configFile: Savable by lazy { DataFile(chiccenAPI, "configs", false) }

    override lateinit var databaseURL: String; private set

    companion object {
        internal lateinit var INSTANCE: ChiccenAPI
        /*
        TODO
            -test debugging tracker - not working

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

    init {
        INSTANCE = this
    }

    override fun load() {
        logger.info("Loading plugin")
    }

    override fun startup() {
        databaseURL = configFile.getOrSet("databaseURL", "mongodb+srv://username:<password>@<host>:<port>?")
        PlayerDeafening

        //load features
        ChiccenCommandFeature.load()

        logger.info("Loaded plugin")
    }

    override fun shutdown() {
        logger.info("Shut down plugin")
    }
}