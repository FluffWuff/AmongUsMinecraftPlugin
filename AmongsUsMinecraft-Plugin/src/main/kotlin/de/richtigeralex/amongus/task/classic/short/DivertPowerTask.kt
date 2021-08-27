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
package de.richtigeralex.amongus.task.classic.short

import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.player.CrewMatePlayer
import de.richtigeralex.amongus.task.AmongUsTaskManager
import de.richtigeralex.amongus.util.inventory.InventoryBuilder
import de.richtigeralex.amongus.util.itembuilder.ItemBuilder
import de.richtigeralex.amongus.util.itembuilder.ItemBuilderManager
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

data class DivertPowerTask(override val taskName: String, override val locations: MutableList<Location>, override val amongUsPlayer: CrewMatePlayer, override val taskManager: AmongUsTaskManager) : ShortTask {


    override val stages: Int = 2
    override var currentStage: Int = 1
    override var hologram: Hologram = HologramsAPI.createHologram(AmongUs.instance, locations[currentStage - 1])
    override var isFinished: Boolean = false

    var firstStageSwitchGlassPaneSlot: Int? = null

    init {
        setupHologram()
    }

    override fun solveTask() {
        val items = mutableMapOf<Int, ItemStack>()
        val blackGlassPane = ItemBuilder(material = Material.BLACK_STAINED_GLASS_PANE).build()
        when (currentStage) {
            1 -> {
                items += intArrayOf(3, 4, 5, 9, 10, 11, 13, 15, 16, 17, 18, 20, 22, 24, 26, 27, 29, 31, 33, 35, 36, 38, 40, 42, 44).associateWith { blackGlassPane }.toMutableMap()
                if(firstStageSwitchGlassPaneSlot == null) firstStageSwitchGlassPaneSlot = intArrayOf(27, 29, 31, 33, 35).random()

                items.replace(firstStageSwitchGlassPaneSlot!!, ItemBuilder(material = Material.RED_STAINED_GLASS_PANE, displayName = "§cSwitch me!", clickHandler = {
                    it.isCancelled = true
                    ItemBuilderManager.itemStacks.remove(it.currentItem!!.itemMeta!!.persistentDataContainer.get(ItemBuilderManager.namespacedKey, PersistentDataType.STRING))
                    it.inventory.setItem(it.slot, ItemBuilder(material = Material.GREEN_STAINED_GLASS_PANE, displayName = "§aSwitched!").build())
                    amongUsPlayer.player.playSound(amongUsPlayer.player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.5F, 1.0F)
                    amongUsPlayer.player.sendMessage("§7$taskName §7(§e1§7/§62§7)")

                    currentStage++
                    hologram.teleport(locations[currentStage - 1])
                }).build())
                amongUsPlayer.player.openInventory(InventoryBuilder(name = taskName, size = 9 * 5, items = items, player = amongUsPlayer.player).inventory)
            }
            2 -> {
                items += intArrayOf(1, 2, 8, 9, 12, 14, 16, 24).associateWith { blackGlassPane }.toMutableMap()
                items[13] = ItemBuilder(material = Material.RED_STAINED_GLASS_PANE, displayName = "§cSwitch me!", clickHandler = {
                    it.isCancelled = true
                    ItemBuilderManager.itemStacks.remove(it.currentItem!!.itemMeta!!.persistentDataContainer.get(ItemBuilderManager.namespacedKey, PersistentDataType.STRING))
                    it.inventory.setItem(it.slot, ItemBuilder(material = Material.GREEN_STAINED_GLASS_PANE, displayName = "§aSwitched!").build())
                    amongUsPlayer.player.playSound(amongUsPlayer.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.5F, 1.0F)
                    amongUsPlayer.player.sendMessage("§7$taskName §7(§e2§7/§62§7)")
                    amongUsPlayer.player.sendMessage("§7$taskName §acompleted!")

                    isFinished = true
                    taskManager.finishTask(this)
                }).build()
                amongUsPlayer.player.openInventory(InventoryBuilder(name = taskName, size = 9 * 3, items = items, player = amongUsPlayer.player).inventory)
            }
        }

    }

}