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