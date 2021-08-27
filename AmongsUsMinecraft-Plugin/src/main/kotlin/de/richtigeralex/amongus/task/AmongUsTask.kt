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
package de.richtigeralex.amongus.task

import com.gmail.filoghost.holographicdisplays.api.Hologram
import de.richtigeralex.amongus.map.AmongUsMap
import de.richtigeralex.amongus.player.AmongUsPlayer
import de.richtigeralex.amongus.player.CrewMatePlayer
import de.richtigeralex.amongus.task.classic.ClassicTask
import de.richtigeralex.amongus.task.classic.long.InspectSampleTask
import de.richtigeralex.amongus.task.classic.short.DivertPowerTask
import org.bukkit.Location
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class AmongUsTaskManager(amongUsMap: AmongUsMap, crewMatePlayers: MutableList<CrewMatePlayer>) {

    val pendingTasks = mutableMapOf<CrewMatePlayer, MutableList<ClassicTask>>()

    init {
        initializeTasks(amongUsMap, crewMatePlayers)
    }


    fun finishTask(task: ClassicTask) {
        val isFinished = task.isFinished
        if (!isFinished) return
        pendingTasks[task.amongUsPlayer]!!.remove(task)
        task.hologram.delete()
        if (pendingTasks[task.amongUsPlayer]!!.isEmpty()) pendingTasks.remove(task.amongUsPlayer)
    }

    private fun initializeTasks(amongUsMap: AmongUsMap, players: MutableList<CrewMatePlayer>) {
        val taskNames = TaskLocationManager.taskLocations[amongUsMap]!!
        players.forEach { crewMatePlayer ->
            val tasks = mutableListOf<ClassicTask>()
            while (tasks.size < 2) {
                val taskName = taskNames.keys.random()
                if (tasks.any { it.taskName == taskName }) continue
                when (taskName) {
                    //"FixWiringTask" -> tasks += FixWiringTask(taskName, taskNames[taskName]!!, crewMatePlayer)
                    //"SwipeCardTask" -> tasks += SwipeCardTask(taskName, taskNames[taskName]!!, crewMatePlayer)
                    "InspectSampleTask" -> tasks += InspectSampleTask(taskName, taskNames[taskName]!!, crewMatePlayer, this)
                    "DivertPowerTask" -> tasks += DivertPowerTask(taskName, taskNames[taskName]!!, crewMatePlayer, this)
                    //"SubmitScanTask" -> tasks += SubmitScanTask(taskName, taskNames[taskName]!!, crewMatePlayer)
                }
            }
            crewMatePlayer.player.sendMessage("Deine Tasks:")
            tasks.forEach { crewMatePlayer.player.sendMessage(it.taskName) }
            pendingTasks[crewMatePlayer] = tasks
        }
    }

    object TaskLocationManager {
        val taskLocations: MutableMap<AmongUsMap, MutableMap<String, MutableList<Location>>> = mutableMapOf()

        fun saveLocations(amongUsMap: AmongUsMap) {
            val file = File(amongUsMap.taskLocationsPath)
            if (!file.exists()) {
                if(!File(amongUsMap.taskLocationsPath.removeSuffix("taskLocation.yml")).exists()) File(amongUsMap.taskLocationsPath.removeSuffix("taskLocation.yml")).mkdir()
                file.createNewFile()
            }
            val yamlFile: FileConfiguration = YamlConfiguration.loadConfiguration(file)
            yamlFile.set("tasks", taskLocations[amongUsMap]!!.keys.toMutableList())
            yamlFile.createSection("locations", taskLocations[amongUsMap]!!)
            yamlFile.save(file)
        }

        fun loadLocations(amongUsMap: AmongUsMap) {
            val file = File(amongUsMap.taskLocationsPath)
            if (!file.exists()) {

                return
            }
            val config = YamlConfiguration.loadConfiguration(file)
            config.getStringList("tasks").forEach {
                taskLocations[amongUsMap]!![it as String] = config.getConfigurationSection("locations")!!.getList(it) as MutableList<Location>
            }
        }

    }

}

interface IAmongUsTask {

    val taskName: String
    val locations: MutableList<Location>
    var hologram: Hologram
    var isFinished: Boolean
    val amongUsPlayer: AmongUsPlayer

    fun setupHologram()

    fun solveTask()


}