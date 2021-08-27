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
package de.richtigeralex.amongus.task.classic

import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler
import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.player.AmongUsPlayer
import de.richtigeralex.amongus.player.CrewMatePlayer
import de.richtigeralex.amongus.task.AmongUsTaskManager
import de.richtigeralex.amongus.task.IAmongUsTask
import org.bukkit.Bukkit
import org.bukkit.Location

interface ClassicTask : IAmongUsTask {

    override var hologram: Hologram
    override val amongUsPlayer: CrewMatePlayer

    val taskManager: AmongUsTaskManager

    val stages: Int
    var currentStage: Int

    override fun setupHologram() {
        Bukkit.getOnlinePlayers().forEach {
            if (it != amongUsPlayer.player) hologram.visibilityManager.hideTo(it)
        }
        hologram.appendTextLine("§a$taskName")
        hologram.appendTextLine("§7(§e${currentStage}§7/§6${stages}§7)").touchHandler = TouchHandler {
            solveTask()
        }
    }

}