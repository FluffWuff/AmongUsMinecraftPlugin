package de.richtigeralex.amongus.map.yaml

import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.map.AmongUsMap
import de.richtigeralex.amongus.map.IAmongUsMapManager
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

class YamlAmongUsMapManager : IAmongUsMapManager {

    override val loadedMaps: MutableList<AmongUsMap> = mutableListOf()
    private val instance = AmongUs.instance

    override fun loadMaps() {
        File("${instance.dataFolder}/maps/").walk().forEach {
            if(it.extension == "yml") {
                val yamlFile: FileConfiguration = YamlConfiguration.loadConfiguration(it)
                loadedMaps.add(AmongUsMap.deserialize(yamlFile.getConfigurationSection("map")!!.getValues(true)))
            }
        }
        loadedMaps.forEach { println("${it.name} successfully loaded") }
    }

    override fun saveMap(amongUsMap: AmongUsMap) {
        val file = File("${instance.dataFolder}/maps/${amongUsMap.name}.yml")
        if(!file.exists()) // case: map is new -> create new file
            file.createNewFile()

        val yamlFile: FileConfiguration = YamlConfiguration.loadConfiguration(file)
        yamlFile.createSection("map", amongUsMap.serialize())
        yamlFile.save(file)
    }

    override fun createMap(mapName: String, creator: Player) {
        val amongUsMap = AmongUsMap(name = mapName, emergencyButtonLocation = creator.getTargetBlock(null, 5).location)
        loadedMaps.add(amongUsMap)
        println("Added map $mapName by ${creator.name}")
    }
}