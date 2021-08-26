/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.richtigeralex.amongus.gamestate

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

    val gameStates: Array<GameState?> = arrayOfNulls(2)
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


