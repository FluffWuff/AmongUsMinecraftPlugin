package de.richtigeralex.amongus

import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.listener.PlayerHandleListener
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.util.itembuilder.ItemBuilderManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class AmongUs : JavaPlugin() {

    companion object {
        @JvmStatic
        lateinit var instance: AmongUs
    }

    init {
        instance = this
    }

    override fun onEnable() {
        println("1")
        val gameStateManager = GameStateManager(this)
        gameStateManager.setGameState(GameState.LOBBY_STATE)
        println("2")
        val playerManager = AmongUsPlayerManager()
        Bukkit.getPluginManager().registerEvents(PlayerHandleListener(gameStateManager, playerManager), this)
        Bukkit.getPluginManager().registerEvents(ItemBuilderManager.ItemBuilderListener(), this)
        println("3")
    }

}