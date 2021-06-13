package de.richtigeralex.amongus.player

import de.richtigeralex.amongus.player.color.ColorManager
import org.bukkit.entity.Player

class AmongUsPlayerManager(val colorManager: ColorManager) {

    val lobbyPlayers: MutableList<LobbyPlayer> = mutableListOf()
    val inGamePlayers: MutableList<AmongUsPlayer> = mutableListOf()
    val spectators: MutableList<Player> = mutableListOf()

    fun convertToInGameState() {
        TODO("Write random pick generator for Imposter/Crewmate with variety at playercount")
    }

    fun convertToLobbyState() {
        inGamePlayers.forEach {
            lobbyPlayers.add(it.toLobbyPlayer())
        }
        spectators.forEach {
            lobbyPlayers.add(LobbyPlayer(colorManager.selectRandomAvailableColor(), it))
        }
    }

}

fun AmongUsPlayer.toLobbyPlayer(): LobbyPlayer = LobbyPlayer(this.color, this.player)

fun LobbyPlayer.toCrewMate(): CrewMatePlayer = CrewMatePlayer(this.color, this.player)

fun LobbyPlayer.toImposter(): ImposterPlayer = ImposterPlayer(this.color, this.player)