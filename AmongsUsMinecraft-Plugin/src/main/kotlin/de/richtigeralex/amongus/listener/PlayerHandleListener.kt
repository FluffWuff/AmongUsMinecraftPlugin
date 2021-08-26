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
package de.richtigeralex.amongus.listener

import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.gamestate.InGameState
import de.richtigeralex.amongus.gamestate.LobbyState
import de.richtigeralex.amongus.map.VoteMapManager
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.LobbyPlayer
import de.richtigeralex.amongus.player.color.ColorManager
import de.richtigeralex.amongus.util.extension.setUpLobby
import de.richtigeralex.amongus.util.extension.setUpSpectator
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerHandleListener(val gameStateManager: GameStateManager, val playerManager: AmongUsPlayerManager, val voteMapManager: VoteMapManager) : Listener {

    @EventHandler
    fun handlePlayerJoin(event: PlayerJoinEvent) {
        event.player.inventory.clear()
        when (gameStateManager.currentGameState) {
            is LobbyState -> {
                val lobbyPlayer: LobbyPlayer = LobbyPlayer(ColorManager.selectRandomAvailableColor(), event.player)
                playerManager.lobbyPlayers.add(lobbyPlayer)
                event.player.setUpLobby(lobbyPlayer)
                println("Spieler gejoint")
            }
            is InGameState -> {
                playerManager.spectators.add(event.player)
                event.player.setUpSpectator()
            }
        }
    }

    @EventHandler
    fun handlePlayerQuit(event: PlayerQuitEvent) {
        when(gameStateManager.currentGameState) {
            is LobbyState -> {
                val lobbyPlayer: LobbyPlayer = playerManager.lobbyPlayers.find { it.player == event.player }!!
                ColorManager.unselectColor(lobbyPlayer.color)

                if(lobbyPlayer.hasVoted) {
                    voteMapManager.voted.values.forEach {
                        if(it.contains(lobbyPlayer)) it.remove(lobbyPlayer)
                    }
                }

                playerManager.lobbyPlayers.remove(lobbyPlayer)
            }
        }
        event.player.inventory.clear()
    }
}