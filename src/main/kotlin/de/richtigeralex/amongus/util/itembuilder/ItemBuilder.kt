package de.richtigeralex.amongus.util.itembuilder

import de.richtigeralex.amongus.AmongUs
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
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

    val namespacedKey: NamespacedKey = NamespacedKey(AmongUs, "fpcbxrdpaafhuifgrekehosbridrunyi")
    val itemStacks: MutableMap<String, ItemStack> = mutableMapOf()

}

data class ItemBuilder(
    val material: Material,
    val amount: Int = 1,
    val displayName: String = " ",
    val color: Color? = null,
    val itemFlags: List<ItemFlag> = mutableListOf(),
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

        var key: String = generateStringKey()
        while (ItemBuilderManager.itemStacks.containsKey(key)) key = generateStringKey()

        itemMeta.persistentDataContainer.set(ItemBuilderManager.namespacedKey, PersistentDataType.STRING, key)
        itemStack.itemMeta = itemMeta
        return itemStack
    }

    private fun generateStringKey(): String {
        val charPool = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")
        return (1..32).map { Random.nextInt(0, charPool.size) }.joinToString("", transform = charPool::get)
    }

}

