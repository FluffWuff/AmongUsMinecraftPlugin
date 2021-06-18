package de.richtigeralex.amongus

import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.color.ColorManager
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin

object AmongUs : JavaPlugin() {

    override fun onEnable() {
        val gameStateManager = GameStateManager(this)
        gameStateManager.setGameState(GameState.LOBBY_STATE)

        val colorManager = ColorManager()
        val playerManager = AmongUsPlayerManager(colorManager)
    }

}