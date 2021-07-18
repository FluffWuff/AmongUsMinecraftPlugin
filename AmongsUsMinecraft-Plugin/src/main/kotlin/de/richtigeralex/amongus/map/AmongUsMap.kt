package de.richtigeralex.amongus.map

import org.bukkit.Location
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.serialization.ConfigurationSerializable
import java.util.stream.Collectors

data class AmongUsMap(
    var name: String,
    val spawnLocations: MutableList<Location> = mutableListOf(),
    var emergencyButtonLocation: Location = Location(null, 0.0, 0.0, 0.0),
    val taskLocations: MutableList<Location> = mutableListOf(), // TODO change to a GeneralTask as extra class
    val sabotageTaskLocations: MutableList<Location> = mutableListOf() // TODO change to a SabotageTask as extra class
) : ConfigurationSerializable {

    override fun serialize(): MutableMap<String, Any> {
        val serializedMap: MutableMap<String, Any> = mutableMapOf()
        serializedMap["mapName"] = this.name
        serializedMap["spawnLocations"] = this.spawnLocations.stream().map(Location::serialize).collect(Collectors.toList())
        serializedMap["emergencyButtonLocation"] = this.emergencyButtonLocation.serialize()
        serializedMap["taskLocations"] = this.taskLocations.stream().map(Location::serialize).collect(Collectors.toList())
        serializedMap["sabotageTaskLocations"] = this.sabotageTaskLocations.stream().map(Location::serialize).collect(Collectors.toList())
        return serializedMap
    }

    companion object {
        fun deserialize(map: MutableMap<String, Any>): AmongUsMap {
            val emergencyButtonMemorySection = map["emergencyButtonLocation"] as MemorySection
            return AmongUsMap(
                name = map["mapName"] as String,
                spawnLocations = filterLocationLists(map, "spawnLocations"),
                emergencyButtonLocation = Location.deserialize(emergencyButtonMemorySection.getValues(true)),
                taskLocations = filterLocationLists(map, "taskLocations"),
                sabotageTaskLocations = filterLocationLists(map, "sabotageTaskLocations")
            )
        }

        private fun filterLocationLists(map: MutableMap<String, Any>, key: String): MutableList<Location> {
            val value = map[key]
            if(value is MutableList<*>) {
                return value.filterIsInstance<Location>() as MutableList<Location>
            }
            return mutableListOf()
        }
    }
}


