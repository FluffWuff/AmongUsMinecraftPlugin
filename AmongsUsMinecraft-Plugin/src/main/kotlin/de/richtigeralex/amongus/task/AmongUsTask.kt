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
    /* TODO
        ==> edit: changed it in saving the path for the yml of the locations
        implement Tasks
        write command for setting the locations of the tasks and saving it


           saving task locations:
           write private object
           MutableMap<String, MutableList<Location>>
           fun serializeTask(taskName: String, locations: MutableList<Locaiton>)
           fun deserialize(taskName: String): MutableMist<Location>

        done:
        initializeTasks function
        saving the path for the locations
        assign tasks to player in map
    */

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
        pendingTasks.forEach { println("player: ${it.key.player.name}, tasks: ${it.value}") }
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