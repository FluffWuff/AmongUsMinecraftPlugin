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
package de.richtigeralex.amongus.player

import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.player.color.ColorManager
import org.bukkit.entity.Player

class AmongUsPlayerManager(val gameStateManager: GameStateManager) {

    val lobbyPlayers: MutableList<LobbyPlayer> = mutableListOf()
    val inGamePlayers: MutableList<AmongUsPlayer> = mutableListOf()
    val spectators: MutableList<Player> = mutableListOf()

    fun convertToInGameState() {
        if (lobbyPlayers.size <= 7) {
            val imposter = lobbyPlayers.random()
            inGamePlayers.add(imposter.toImposter())
            lobbyPlayers.remove(imposter)
        } else {
            val imposters = lobbyPlayers.take(2)
            imposters.forEach {
                inGamePlayers.add(it.toImposter())
                lobbyPlayers.remove(it)
            }
        }
        lobbyPlayers.forEach {
            inGamePlayers.add(it.toCrewMate())
        }
        lobbyPlayers.clear()
    }

    fun convertToLobbyState() {
        inGamePlayers.forEach {
            lobbyPlayers.add(it.toLobbyPlayer())
        }
        spectators.forEach {
            lobbyPlayers.add(LobbyPlayer(ColorManager.selectRandomAvailableColor(), it))
        }
        inGamePlayers.clear()
        spectators.clear()
    }

}

fun AmongUsPlayer.toLobbyPlayer(): LobbyPlayer = LobbyPlayer(this.color, this.player)

fun LobbyPlayer.toCrewMate(): CrewMatePlayer = CrewMatePlayer(this.color, this.player)

fun LobbyPlayer.toImposter(): ImposterPlayer = ImposterPlayer(this.color, this.player, nextAvailableKill = System.currentTimeMillis() + 30000L)