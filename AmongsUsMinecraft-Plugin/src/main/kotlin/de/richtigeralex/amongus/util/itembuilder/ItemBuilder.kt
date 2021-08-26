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
package de.richtigeralex.amongus.util.itembuilder

import de.richtigeralex.amongus.AmongUs
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

object ItemBuilderManager {

    val namespacedKey: NamespacedKey = NamespacedKey(AmongUs.instance, "fpcbxrdpaafhuifgrekehosbridrunyi")
    val itemStacks: MutableMap<String, ItemBuilder> = mutableMapOf()

    class ItemBuilderListener : Listener {
        @EventHandler
        fun handleClickEvent(event: InventoryClickEvent) {
            if(event.whoClicked !is Player) return
            if(event.currentItem == null) return
            if(!event.currentItem!!.hasItemMeta()) return
            if(!event.currentItem!!.itemMeta!!.persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)) return
            val itemKey: String = event.currentItem!!.itemMeta!!.persistentDataContainer.get(namespacedKey, PersistentDataType.STRING)!!
            if(!itemStacks.containsKey(itemKey)) return
            val itemBuilder: ItemBuilder = itemStacks[itemKey]!!
            itemBuilder.clickHandler.invoke(event)
        }

        @EventHandler
        fun handleDropItemEvent(event: PlayerDropItemEvent) {
            if(!event.itemDrop.itemStack.hasItemMeta()) return
            if(!event.itemDrop.itemStack.itemMeta!!.persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)) return
            val itemKey: String = event.itemDrop.itemStack.itemMeta!!.persistentDataContainer.get(namespacedKey, PersistentDataType.STRING)!!
            if(!itemStacks.containsKey(itemKey)) return
            val itemBuilder: ItemBuilder = itemStacks[itemKey]!!
            itemBuilder.dropHandler.invoke(event)
        }

        @EventHandler
        fun handleInteractEvent(event: PlayerInteractEvent) {
            if(event.item == null) return
            if(!event.item!!.hasItemMeta()) return
            if(!event.item!!.itemMeta!!.persistentDataContainer.has(namespacedKey, PersistentDataType.STRING)) return
            val itemKey: String = event.item!!.itemMeta!!.persistentDataContainer.get(namespacedKey, PersistentDataType.STRING)!!
            if(!itemStacks.containsKey(itemKey)) return
            val itemBuilder: ItemBuilder = itemStacks[itemKey]!!
            itemBuilder.interactHandler.invoke(event)
        }
    }
}

data class ItemBuilder(
    val material: Material,
    val amount: Int = 1,
    val displayName: String = "",
    val color: Color? = null,
    val itemFlags: List<ItemFlag> = mutableListOf(ItemFlag.HIDE_UNBREAKABLE),
    val enchantments: MutableMap<Enchantment, Int> = mutableMapOf(),
    val isUnbreakable: Boolean = true,
    val lore: List<String> = mutableListOf(),
    val clickHandler: (InventoryClickEvent) -> Unit = { it.isCancelled = true },
    val dropHandler: (PlayerDropItemEvent) -> Unit = { it.isCancelled = true },
    val interactHandler: (PlayerInteractEvent) -> Unit = { it.isCancelled = true }
) {

    private val itemStack: ItemStack = ItemStack(material, amount)
    private val itemMeta: ItemMeta = itemStack.itemMeta!!

    fun build(): ItemStack {
        itemMeta.setDisplayName(displayName)
        if (itemMeta is LeatherArmorMeta) itemMeta.setColor(color)
        itemFlags.forEach { itemMeta.addItemFlags(it) }
        itemMeta.isUnbreakable = isUnbreakable
        itemMeta.lore = lore
        enchantments.forEach {
            itemMeta.addEnchant(it.key, it.value, true)
        }
        var key: String = generateStringKey()
        while (ItemBuilderManager.itemStacks.containsKey(key)) key = generateStringKey()

        itemMeta.persistentDataContainer.set(ItemBuilderManager.namespacedKey, PersistentDataType.STRING, key)
        itemStack.itemMeta = itemMeta
        ItemBuilderManager.itemStacks[key] = this
        return itemStack
    }

    private fun generateStringKey(): String {
        val charPool = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")
        return (1..32).map { Random.nextInt(0, charPool.size) }.joinToString("", transform = charPool::get)
    }

}