package de.richtigeralex.amongus.player

import de.richtigeralex.amongus.player.color.ColorManager
import org.bukkit.entity.Player

class AmongUsPlayerManager(val colorManager: ColorManager) {

    val lobbyPlayers: MutableList<LobbyPlayer> = mutableListOf()
    val inGamePlayers: MutableList<AmongUsPlayer> = mutableListOf()
    val spectators: MutableList<Player> = mutableListOf()

    fun convertToInGameState() {
        if (lobbyPlayers.size <= 7) {
            val imposter = lobbyPlayers.random()
            inGamePlayers.add(imposter.toImposter())
            lobbyPlayers.remove(imposter)
        } else {
            val imposter = lobbyPlayers.take(2)
            imposter.forEach {
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
            lobbyPlayers.add(LobbyPlayer(colorManager.selectRandomAvailableColor(), it))
        }
        inGamePlayers.clear()
        spectators.clear()
    }

}

fun AmongUsPlayer.toLobbyPlayer(): LobbyPlayer = LobbyPlayer(this.color, this.player)

fun LobbyPlayer.toCrewMate(): CrewMatePlayer = CrewMatePlayer(this.color, this.player)

fun LobbyPlayer.toImposter(): ImposterPlayer = ImposterPlayer(this.color, this.player)