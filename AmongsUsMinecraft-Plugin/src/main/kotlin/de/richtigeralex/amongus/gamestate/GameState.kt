package de.richtigeralex.amongus.gamestate

import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import org.bukkit.plugin.java.JavaPlugin

abstract class GameState {

    companion object {
        var LOBBY_STATE = 0
        var INGAME_STATE = 1
    }

    abstract fun start()

    abstract fun stop()

}

class GameStateManager(val plugin: JavaPlugin) {

    val gameStates: Array<GameState> = arrayOf()
    var currentGameState: GameState? = null

    fun setGameState(gameStateID: Int) {
        currentGameState?.stop()
        currentGameState = gameStates[gameStateID]
        currentGameState!!.start()
    }

    fun stopCurrentGameState() {
        currentGameState?.stop()
        currentGameState = null
    }

}


