package de.richtigeralex.amongus.player

import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.commands.lobby.LobbyReadyCommand
import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.gamestate.InGameState
import de.richtigeralex.amongus.gamestate.LobbyState
import de.richtigeralex.amongus.listener.PlayerHandleListener
import de.richtigeralex.amongus.player.color.ColorManager
import de.richtigeralex.amongus.player.impostor.ImposterListener
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class AmongUsPlayerManager(val gameStateManager: GameStateManager) {

    val lobbyPlayers: MutableList<LobbyPlayer> = mutableListOf()
    val inGamePlayers: MutableList<AmongUsPlayer> = mutableListOf()
    val spectators: MutableList<Player> = mutableListOf()

    init {
        gameStateManager.gameStates[GameState.LOBBY_STATE] = LobbyState()
        gameStateManager.gameStates[GameState.INGAME_STATE] = InGameState(this)

        Bukkit.getPluginManager().registerEvents(ImposterListener(this), AmongUs.instance)
        Bukkit.getPluginManager().registerEvents(PlayerHandleListener(gameStateManager, this), AmongUs.instance)

        AmongUs.instance.getCommand("r")!!.setExecutor(LobbyReadyCommand(this))
    }

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