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
package de.richtigeralex.amongus.map

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.serialization.ConfigurationSerializable
import java.util.stream.Collectors

data class AmongUsMap(
    var name: String,
    var startSequenceLocation: Location = Location(Bukkit.getWorld("world"), 0.5, 57.0, 10.5, 0.0F, 0.0F),
    val spawnLocations: MutableList<Location> = mutableListOf(),
    var emergencyButtonLocation: Location = Location(null, 0.0, 0.0, 0.0),
    val taskLocationsPath: String, // Path for the file of the saved task locations
    val sabotageTaskLocations: MutableList<Location> = mutableListOf() // TODO change to a SabotageTask as extra class
) : ConfigurationSerializable {

    override fun serialize(): MutableMap<String, Any> {
        val serializedMap: MutableMap<String, Any> = mutableMapOf()
        serializedMap["mapName"] = this.name
        serializedMap["startSequenceLocation"] = this.startSequenceLocation.serialize()
        serializedMap["spawnLocations"] = this.spawnLocations.stream().map(Location::serialize).collect(Collectors.toList())
        serializedMap["emergencyButtonLocation"] = this.emergencyButtonLocation.serialize()
        serializedMap["taskLocationsPath"] = this.taskLocationsPath
        serializedMap["sabotageTaskLocations"] = this.sabotageTaskLocations.stream().map(Location::serialize).collect(Collectors.toList())
        return serializedMap
    }

    companion object {
        fun deserialize(map: MutableMap<String, Any>): AmongUsMap {
            val startSequenceMemorySection = map["startSequenceLocation"] as MemorySection
            val emergencyButtonMemorySection = map["emergencyButtonLocation"] as MemorySection
            return AmongUsMap(
                name = map["mapName"] as String,
                startSequenceLocation = Location.deserialize(startSequenceMemorySection.getValues(true)),
                spawnLocations = filterLocationLists(map, "spawnLocations"),
                emergencyButtonLocation = Location.deserialize(emergencyButtonMemorySection.getValues(true)),
                taskLocationsPath = map["taskLocationsPath"] as String,
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


