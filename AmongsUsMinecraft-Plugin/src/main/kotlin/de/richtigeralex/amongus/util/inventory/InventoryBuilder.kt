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
package de.richtigeralex.amongus.util.inventory

import de.richtigeralex.amongus.task.classic.ClassicTask
import de.richtigeralex.amongus.util.itembuilder.ItemBuilderManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object InventoryBuilderManager {

    val currentInventoriesBuilds: MutableList<InventoryBuilder> = mutableListOf()

    class InventoryBuilderListener : Listener {

        @EventHandler
        fun handleInventoryClose(event: InventoryCloseEvent) {
            val inventoryBuilder: InventoryBuilder = currentInventoriesBuilds.find { it.inventory == event.inventory && it.player == event.player } ?: return
            inventoryBuilder.closeHandler.invoke(event)
            currentInventoriesBuilds.remove(inventoryBuilder)
        }
    }

}

data class InventoryBuilder(
    val name: String,
    val size: Int = 0,
    val items: MutableMap<Int, ItemStack>,
    val inventoryType: InventoryType? = null,
    val player: Player,
    var closeHandler: (InventoryCloseEvent) -> Unit = {}
) {

    val inventory: Inventory = if (inventoryType == null) Bukkit.createInventory(null, size, name) else Bukkit.createInventory(null, inventoryType, name)

    init {
        items.forEach { (k, v) ->
            inventory.setItem(k, v)
        }
        closeHandler = {
            items.values.forEach { itemStack ->
                ItemBuilderManager.itemStacks.remove(itemStack.itemMeta!!.persistentDataContainer.get(ItemBuilderManager.namespacedKey, PersistentDataType.STRING)!!)
            }
            println("${items.values.size} got removed")
        }
        InventoryBuilderManager.currentInventoriesBuilds += this
    }

}
