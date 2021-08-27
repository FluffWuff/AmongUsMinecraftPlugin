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
package de.richtigeralex.amongus.player

import de.richtigeralex.amongus.api.events.ImposterKillEvent
import de.richtigeralex.amongus.task.classic.ClassicTask
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.GameMode
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

data class LobbyPlayer(var color: Color, val player: Player, var isReady: Boolean = false, var hasVoted: Boolean = false)

data class CrewMatePlayer(override var color: Color, override val player: Player, override var isDead: Boolean = false) : AmongUsPlayer

data class ImposterPlayer(override var color: Color, override val player: Player, override var isDead: Boolean = false, var nextAvailableKill: Long) : AmongUsPlayer {

    fun killCrewMate(target: CrewMatePlayer) {
        target.isDead = true
        target.player.gameMode = GameMode.SPECTATOR
        Bukkit.getPluginManager().callEvent(ImposterKillEvent(this, target))
    }

    fun sabotage() {
        TODO()
    }

}