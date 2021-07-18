package de.richtigeralex.amongus.map.json

import de.richtigeralex.amongus.map.AmongUsMap
import de.richtigeralex.amongus.map.IAmongUsMapManager
import org.bukkit.entity.Player

/**
 * Coming soon™
 */
class JsonAmongUsMapManager : IAmongUsMapManager {

    override val loadedMaps: MutableList<AmongUsMap> = mutableListOf()

    override fun loadMaps() {
        TODO("Not yet implemented")
    }

    override fun saveMap(amongUsMap: AmongUsMap) {
        TODO("Not yet implemented")
    }

    override fun createMap(mapName: String, creator: Player) {
        TODO("Not yet implemented")
    }
}