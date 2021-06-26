package de.richtigeralex.amongus.player

import org.bukkit.Color
import org.bukkit.entity.Player

interface AmongUsPlayer {

    var color: Color
    val player: Player
    var isDead: Boolean

    fun triggerEmergencyMeeting() {
        TODO()
    }

    fun reportDeadBody() {
        TODO()
    }

}

data class LobbyPlayer(var color: Color, val player: Player)

data class CrewMatePlayer(override var color: Color, override val player: Player, override var isDead: Boolean = false) : AmongUsPlayer {

    fun completeTask() {
        TODO()
    }

}

data class ImposterPlayer(override var color: Color, override val player: Player, override var isDead: Boolean = false) : AmongUsPlayer {

    val nextAvailableKill: MutableMap<ImposterPlayer, Long> = mutableMapOf()

    fun killCrewMate(target: CrewMatePlayer) {
        if(nextAvailableKill[this]!! >= System.currentTimeMillis()) {
            target.isDead = true
            /*
                TODO: add future event
             */

        }
    }

    fun sabotage() {
        TODO()
    }

}