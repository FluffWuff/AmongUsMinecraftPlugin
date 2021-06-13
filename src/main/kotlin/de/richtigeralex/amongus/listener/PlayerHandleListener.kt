package de.richtigeralex.amongus.listener

import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.gamestate.InGameState
import de.richtigeralex.amongus.gamestate.LobbyState
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.LobbyPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerHandleListener(val gameStateManager: GameStateManager, val playerManager: AmongUsPlayerManager): Listener {

    @EventHandler
    fun handlePlayerJoin(event: PlayerJoinEvent) {
        when(gameStateManager.currentGameState) {
            is LobbyState -> {
                playerManager.lobbyPlayers.add(LobbyPlayer(playerManager.colorManager.selectRandomAvailableColor(), event.player))
            }
            is InGameState -> {
                playerManager.spectators.add(event.player)
            }
        }
    }

}