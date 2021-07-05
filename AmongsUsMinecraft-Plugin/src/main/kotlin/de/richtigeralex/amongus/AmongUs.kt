package de.richtigeralex.amongus

import de.richtigeralex.amongus.commands.lobby.LobbyReadyCommand
import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.listener.CancelUselessListener
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
        val gameStateManager = GameStateManager(this)

        val playerManager = AmongUsPlayerManager(gameStateManager)
        gameStateManager.setGameState(GameState.LOBBY_STATE)

        Bukkit.getPluginManager().registerEvents(ItemBuilderManager.ItemBuilderListener(), this)
        Bukkit.getPluginManager().registerEvents(CancelUselessListener(), this)
    }

}