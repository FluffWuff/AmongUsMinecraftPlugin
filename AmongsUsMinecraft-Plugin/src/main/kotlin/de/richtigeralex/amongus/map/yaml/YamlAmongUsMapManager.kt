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
package de.richtigeralex.amongus.map.yaml

import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.map.AmongUsMap
import de.richtigeralex.amongus.map.IAmongUsMapManager
import de.richtigeralex.amongus.task.AmongUsTaskManager
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

class YamlAmongUsMapManager : IAmongUsMapManager {

    override val loadedMaps: MutableList<AmongUsMap> = mutableListOf()
    override var selectedMap: AmongUsMap? = null
    private val instance = AmongUs.instance

    override fun loadMaps() {
        File("${instance.dataFolder}/maps/").walk().forEach {
            if (it.extension == "yml" && it.parentFile == File("${instance.dataFolder}/maps/")) {
                val yamlFile: FileConfiguration = YamlConfiguration.loadConfiguration(it)
                loadedMaps.add(AmongUsMap.deserialize(yamlFile.getConfigurationSection("map")!!.getValues(true)))
            }
        }
        loadedMaps.forEach {
            println("${it.name} successfully loaded")
            AmongUsTaskManager.TaskLocationManager.taskLocations[it] = mutableMapOf()
            AmongUsTaskManager.TaskLocationManager.loadLocations(it)
            println("loaded ${it.name} Tasklocations")
        }
    }

    override fun saveMap(amongUsMap: AmongUsMap) {
        AmongUsTaskManager.TaskLocationManager.saveLocations(amongUsMap)

        val file = File("${instance.dataFolder}/maps/${amongUsMap.name}.yml")
        if (!file.exists()) // case: map is new -> create new file
            file.createNewFile()

        val yamlFile: FileConfiguration = YamlConfiguration.loadConfiguration(file)
        yamlFile.createSection("map", amongUsMap.serialize())
        yamlFile.save(file)
    }

    override fun createMap(mapName: String, creator: Player) {
        val amongUsMap = AmongUsMap(name = mapName, emergencyButtonLocation = creator.getTargetBlock(null, 5).location, taskLocationsPath = "${instance.dataFolder}/maps/$mapName/taskLocation.yml")
        loadedMaps.add(amongUsMap)
        AmongUsTaskManager.TaskLocationManager.taskLocations[amongUsMap] = mutableMapOf()
        println("Added map $mapName by ${creator.name}")
    }
}