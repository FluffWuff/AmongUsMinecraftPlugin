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
package de.richtigeralex.amongus.util.extension

import de.richtigeralex.amongus.player.LobbyPlayer
import de.richtigeralex.amongus.player.color.ColorManager
import de.richtigeralex.amongus.util.itembuilder.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

fun Player.setUpLobby(lobbyPlayer: LobbyPlayer) {
    val colorDisplayName: String = lobbyPlayer.color.toColorPrefix()
    this.inventory.helmet = ItemBuilder(material = Material.LEATHER_HELMET, color = lobbyPlayer.color, displayName = colorDisplayName).build()
    this.inventory.chestplate = ItemBuilder(material = Material.LEATHER_CHESTPLATE, color = lobbyPlayer.color, displayName = colorDisplayName).build()
    this.inventory.leggings = ItemBuilder(material = Material.LEATHER_LEGGINGS, color = lobbyPlayer.color, displayName = colorDisplayName).build()
    this.inventory.boots = ItemBuilder(material = Material.LEATHER_BOOTS, color = lobbyPlayer.color, displayName = colorDisplayName).build()

    this.inventory.setItem(4, ItemBuilder(material = Material.NETHER_STAR, displayName = "§5Farbauswahl", interactHandler = { it ->
        val inventory: Inventory = Bukkit.createInventory(null, 9*4, "§5Farbauswahl")
        val inventoryItems = getColorSelectInventoryItems(lobbyPlayer)

        inventoryItems.forEach { inventory.setItem(it.key, it.value) }
        it.player.openInventory(inventory)
    }).build())
    this.updateInventory()
}

fun Player.setUpSpectator() {

}

private fun getColorSelectInventoryItems(lobbyPlayer: LobbyPlayer): MutableMap<Int, ItemStack> {
    val availableColors: MutableList<Color> = ColorManager.availableColors
    val barrierSlots: MutableList<Int> = mutableListOf(10,11,12,14,15,16,19,20,21,23,24,25)
    val inventoryItems: MutableMap<Int, ItemStack> = mutableMapOf()

    for (color: Color in availableColors) {
        inventoryItems[color.toColorSlotInventory()] = ItemBuilder(material = color.toConcreteMaterial(), displayName = color.toColorPrefix(), clickHandler = {
            ColorManager.selectColor(color, lobbyPlayer.color)
            lobbyPlayer.color = color
            lobbyPlayer.player.setUpLobby(lobbyPlayer)
            it.whoClicked.closeInventory()
        }).build()
        barrierSlots.remove(color.toColorSlotInventory())
    }

    val barrier: ItemStack = ItemBuilder(material = Material.BARRIER, displayName = "§cVergeben").build()
    barrierSlots.forEach { inventoryItems[it] = barrier }

    val pickedColor: ItemStack = ItemBuilder(material = lobbyPlayer.color.toConcreteMaterial(), displayName = lobbyPlayer.color.toColorPrefix(), itemFlags = mutableListOf(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE), enchantments = mutableMapOf(Pair(Enchantment.DURABILITY, 1))).build()
    inventoryItems[lobbyPlayer.color.toColorSlotInventory()] = pickedColor
    return inventoryItems
}