package de.richtigeralex.amongus.listener

import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.gamestate.InGameState
import de.richtigeralex.amongus.gamestate.LobbyState
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.LobbyPlayer
import de.richtigeralex.amongus.util.extension.setUpLobby
import de.richtigeralex.amongus.util.extension.setUpSpectator
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerHandleListener(val gameStateManager: GameStateManager, val playerManager: AmongUsPlayerManager) : Listener {

    @EventHandler
    fun handlePlayerJoin(event: PlayerJoinEvent) {
        when (gameStateManager.currentGameState) {
            is LobbyState -> {
                val lobbyPlayer: LobbyPlayer = LobbyPlayer(playerManager.colorManager.selectRandomAvailableColor(), event.player)
                playerManager.lobbyPlayers.add(lobbyPlayer)
                event.player.setUpLobby(lobbyPlayer)
            }
            is InGameState -> {
                playerManager.spectators.add(event.player)
                event.player.setUpSpectator()
            }
        }
    }

}