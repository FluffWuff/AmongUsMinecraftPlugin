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
package de.richtigeralex.amongus.task.classic.long

import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.player.CrewMatePlayer
import de.richtigeralex.amongus.task.AmongUsTaskManager
import de.richtigeralex.amongus.util.inventory.InventoryBuilder
import de.richtigeralex.amongus.util.itembuilder.ItemBuilder
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound

data class InspectSampleTask(
    override val taskName: String = "InspectSampleTask",
    override val locations: MutableList<Location> = mutableListOf(),
    override val amongUsPlayer: CrewMatePlayer,
    override val taskManager: AmongUsTaskManager
) : LongTask {

    /**
     * three stages:
     * first stage: task not started
     * second: task started but wait time
     * third: wait time is finished
     */
    override val stages: Int = 3
    override var currentStage: Int = 1
    override var hologram: Hologram = HologramsAPI.createHologram(AmongUs.instance, locations[currentStage - 1])

    override var isFinished: Boolean = false

    /**
     * first and second are the anomaly glass pane
     * third is the white glass pane to click
     */
    var anomalyTriple: Triple<Int, Int, Int>? = null

    init {
        setupHologram()
    }


    override fun solveTask() {
        val items = intArrayOf(
            9, 11, 12, 14, 15, 17,
            18, 20, 21, 23, 24, 26,
            27, 29, 30, 32, 33, 35,
            37, 40, 43
        ).associateWith { ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).build() }.toMutableMap()
        items += intArrayOf(
            46, 49, 52
        ).associateWith {
            ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, clickHandler = { event ->
                if (currentStage != 3) return@ItemBuilder
                if (anomalyTriple!!.third != event.slot) {
                    amongUsPlayer.player.playSound(amongUsPlayer.player.location, Sound.ENTITY_ENDERMAN_HURT, 1.5F, 1.0F)
                    amongUsPlayer.player.sendMessage("§cNo anomaly!")
                    return@ItemBuilder
                }
                amongUsPlayer.player.playSound(amongUsPlayer.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.5F, 1.0F)
                amongUsPlayer.player.sendMessage("§7$taskName §7(§e1§7/§61§7)")
                amongUsPlayer.player.sendMessage("§7$taskName §acompleted!")

                isFinished = true
                taskManager.finishTask(this)
            }).build()
        }.toMutableMap()


        when (stages) {
            1 -> {
                items += intArrayOf(
                    19, 22, 25,
                    28, 31, 34,
                ).associateWith { ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).build() }
                items[53] = ItemBuilder(Material.RED_STAINED_GLASS_PANE, displayName = "§cActivate me!", clickHandler = { event ->
                    event.inventory.setItem(event.slot, ItemBuilder(material = Material.GREEN_STAINED_GLASS_PANE, displayName = "§aActivated!").build())

                    val pairs = arrayOf(Pair(19, 28), Pair(22, 31), Pair(25, 34))
                    val blueGlassPane = ItemBuilder(Material.BLUE_STAINED_GLASS_PANE, displayName = "§1§krandom").build()

                    repeat(3) { // try to change that async if possible
                        amongUsPlayer.player.playSound(amongUsPlayer.player.location, Sound.BLOCK_BREWING_STAND_BREW, 1.5F, 1.0F)
                        event.inventory.setItem(pairs[it].second, blueGlassPane)
                        Thread.sleep(250L)
                        event.inventory.setItem(pairs[it].first, blueGlassPane)
                        Thread.sleep(900L)
                    }
                }).build()
            }
            2 -> {
                items += intArrayOf(
                    19, 22, 25,
                    28, 31, 34,
                ).associateWith { ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).build() }
                items[53] = ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).build()
            }
            3 -> {
                val triples = arrayOf(Triple(19, 28, 46), Triple(22, 31, 49), Triple(25, 34, 52))
                if (anomalyTriple == null) anomalyTriple = triples.random()

                val redGlassPane = ItemBuilder(Material.RED_STAINED_GLASS_PANE, displayName = "§csus", lore = mutableListOf("oh oh", "click on the white glass pane below!")).build()
                items[anomalyTriple!!.first] = redGlassPane
                items[anomalyTriple!!.second] = redGlassPane
            }
        }
        amongUsPlayer.player.openInventory(InventoryBuilder(name = taskName, size = 9 * 6, items = items, task = this).inventory)

    }

}