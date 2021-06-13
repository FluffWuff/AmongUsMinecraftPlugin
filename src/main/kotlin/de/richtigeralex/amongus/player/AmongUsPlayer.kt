package de.richtigeralex.amongus.player

import org.bukkit.Color
import org.bukkit.entity.Player

interface AmongUsPlayer {

    var color: Color
    val player: Player

    fun triggerEmergencyMeeting() {
        TODO()
    }

    fun reportDeadBody() {
        TODO()
    }

}

data class LobbyPlayer(var color: Color, val player: Player)

data class CrewMatePlayer(override var color: Color, override val player: Player) : AmongUsPlayer {

    fun completeTask() {
        TODO()
    }

}

data class ImposterPlayer(override var color: Color, override val player: Player) : AmongUsPlayer{

    fun killCrewMate() {
        TODO()
    }

    fun sabotage() {
        TODO()
    }

}