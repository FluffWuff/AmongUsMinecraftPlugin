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
package de.richtigeralex.amongus.commands.general

import de.richtigeralex.amongus.map.IAmongUsMapManager
import de.richtigeralex.amongus.task.AmongUsTaskManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TaskLocationCommand(private val amongUsMapManager: IAmongUsMapManager) : CommandExecutor {

    //usage /taskLocation <add/remove> <amongusmap> <taskname>
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        if (args.size < 3) return true
        val amongUsMap = amongUsMapManager.loadedMaps.find { it.name == args[1] }
        when (args[0]) {
            "add" -> { // || AmongUsTaskManager.TaskLocationManager.taskLocations[amongUsMap]!![args[2]] == null
                if (AmongUsTaskManager.TaskLocationManager.taskLocations[amongUsMap]!![args[2]] == null)
                    AmongUsTaskManager.TaskLocationManager.taskLocations[amongUsMap]!![args[2]] = mutableListOf(sender.eyeLocation)
                else
                    AmongUsTaskManager.TaskLocationManager.taskLocations[amongUsMap]!![args[2]]?.plusAssign(sender.eyeLocation)
            }
            "remove" -> {
                AmongUsTaskManager.TaskLocationManager.taskLocations[amongUsMapManager.loadedMaps.find { it.name == args[1] }]!![args[2]]?.remove(sender.eyeLocation)
            }
        }
        return true
    }
}