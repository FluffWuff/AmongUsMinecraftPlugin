package de.richtigeralex.amongus.player

import de.richtigeralex.amongus.api.events.ImposterKillEvent
import org.bukkit.Bukkit
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

data class LobbyPlayer(var color: Color, val player: Player, var isReady: Boolean = false)

data class CrewMatePlayer(override var color: Color, override val player: Player, override var isDead: Boolean = false) : AmongUsPlayer {

    fun completeTask() {
        TODO()
    }

}

data class ImposterPlayer(override var color: Color, override val player: Player, override var isDead: Boolean = false, var nextAvailableKill: Long) : AmongUsPlayer {

    fun killCrewMate(target: CrewMatePlayer) {
        target.isDead = true

        Bukkit.getPluginManager().callEvent(ImposterKillEvent(this, target))
    }

    fun sabotage() {
        TODO()
    }

}