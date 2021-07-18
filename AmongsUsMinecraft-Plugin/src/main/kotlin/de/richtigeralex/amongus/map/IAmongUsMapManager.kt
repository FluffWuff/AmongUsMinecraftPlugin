package de.richtigeralex.amongus.map

import org.bukkit.entity.Player

interface IAmongUsMapManager {

    val loadedMaps: MutableList<AmongUsMap>

    fun loadMaps()

    fun saveMap(amongUsMap: AmongUsMap)

    fun saveAllMaps() = loadedMaps.forEach{ saveMap(it) }

    fun createMap(mapName: String, creator: Player)

}