package de.richtigeralex.amongus

import de.richtigeralex.amongus.commands.general.CreateNewMapCommand
import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.listener.CancelUselessListener
import de.richtigeralex.amongus.map.AmongUsMap
import de.richtigeralex.amongus.map.IAmongUsMapManager
import de.richtigeralex.amongus.map.yaml.YamlAmongUsMapManager
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.util.itembuilder.ItemBuilderManager
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class AmongUs : JavaPlugin() {

    companion object {
        @JvmStatic
        lateinit var instance: AmongUs
    }

    private val amongUsMapManager: IAmongUsMapManager

    init {
        instance = this
        val serializableClasses = arrayOf(AmongUsMap::class.java) // more classes in future
        Arrays.stream(serializableClasses).forEach(ConfigurationSerialization::registerClass)
        amongUsMapManager = YamlAmongUsMapManager()
    }

    override fun onEnable() {
        amongUsMapManager.loadMaps()
        println("Loaded all maps")

        val gameStateManager: GameStateManager = GameStateManager(this)
        val amongUsPlayerManager: AmongUsPlayerManager = AmongUsPlayerManager(gameStateManager)

        gameStateManager.setGameState(GameState.LOBBY_STATE)
        println("started Lobby State")

        Bukkit.getPluginManager().registerEvents(ItemBuilderManager.ItemBuilderListener(), this)
        Bukkit.getPluginManager().registerEvents(CancelUselessListener(), this)

        getCommand("createMap")!!.setExecutor(CreateNewMapCommand(amongUsMapManager))
    }

    override fun onDisable() {
        amongUsMapManager.saveAllMaps()
    }

}