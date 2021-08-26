package de.richtigeralex.amongus.util.inventory

import de.richtigeralex.amongus.task.classic.ClassicTask
import de.richtigeralex.amongus.util.itembuilder.ItemBuilder
import de.richtigeralex.amongus.util.itembuilder.ItemBuilderManager
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

data class InventoryBuilder(
    val name: String,
    val size: Int = 0,
    val items: MutableMap<Int, ItemStack>,
    val inventoryType: InventoryType? = null,
    val task: ClassicTask,
    var closeHandler: (InventoryCloseEvent) -> Unit = {}
) {

    val inventory: Inventory = if (inventoryType == null) Bukkit.createInventory(null, size, name) else Bukkit.createInventory(null, inventoryType, name)

    init {
        items.forEach { (k, v) ->
            inventory.setItem(k, v)
        }
        closeHandler = {
            if (it.inventory == inventory && it.player == task.amongUsPlayer.player) {
                items.values.forEach { itemStack ->
                    ItemBuilderManager.itemStacks.remove(itemStack.itemMeta!!.persistentDataContainer.get(ItemBuilderManager.namespacedKey, PersistentDataType.STRING)!!)
                }
                println("${items.values.size} got removed")
            }
        }
    }

}
