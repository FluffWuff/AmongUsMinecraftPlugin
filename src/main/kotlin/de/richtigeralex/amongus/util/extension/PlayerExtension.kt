package de.richtigeralex.amongus.util.extension

import de.richtigeralex.amongus.player.LobbyPlayer
import de.richtigeralex.amongus.player.color.ColorManager
import de.richtigeralex.amongus.util.itembuilder.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

fun Player.setUpLobby(lobbyPlayer: LobbyPlayer) {
    this.inventory.helmet = ItemBuilder(material = Material.LEATHER_HELMET, color = lobbyPlayer.color).build()
    this.inventory.chestplate = ItemBuilder(material = Material.LEATHER_CHESTPLATE, color = lobbyPlayer.color).build()
    this.inventory.leggings = ItemBuilder(material = Material.LEATHER_LEGGINGS, color = lobbyPlayer.color).build()
    this.inventory.boots = ItemBuilder(material = Material.LEATHER_BOOTS, color = lobbyPlayer.color).build()

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
        inventoryItems[getSlot(color)] = ItemBuilder(material = getColorMaterial(color), clickHandler = {
            ColorManager.selectColor(color, lobbyPlayer.color)
            lobbyPlayer.color = color
            lobbyPlayer.player.setUpLobby(lobbyPlayer)
            it.whoClicked.closeInventory()
        }).build() //TODO write clickHandler for the items (add ColorSwitch)
        barrierSlots.remove(getSlot(color))
    }

    val barrier: ItemStack = ItemBuilder(material = Material.BARRIER, displayName = "§cVergeben").build()
    barrierSlots.forEach { inventoryItems[it] = barrier }

    val pickedColor: ItemStack = ItemBuilder(material = getColorMaterial(lobbyPlayer.color), itemFlags = mutableListOf(ItemFlag.HIDE_ENCHANTS), enchantments = mutableMapOf(Pair(Enchantment.DURABILITY, 1))).build()
    inventoryItems[getSlot(lobbyPlayer.color)] = pickedColor
    return inventoryItems
}

fun getColorMaterial(color: Color): Material {
    when (color) {
        Color.RED -> return Material.RED_CONCRETE
        Color.BLUE -> return Material.BLUE_CONCRETE
        Color.GREEN -> return Material.GREEN_CONCRETE
        Color.FUCHSIA -> return Material.PINK_CONCRETE
        Color.ORANGE -> return Material.ORANGE_CONCRETE
        Color.YELLOW -> return Material.YELLOW_CONCRETE
        Color.BLACK -> return Material.BLACK_CONCRETE
        Color.WHITE -> return Material.WHITE_CONCRETE
        Color.PURPLE -> return Material.PURPLE_CONCRETE
        Color.GRAY -> return Material.GRAY_CONCRETE
        Color.AQUA -> return Material.LIGHT_BLUE_CONCRETE
        Color.LIME -> return Material.LIME_CONCRETE
    }
    return Material.NETHER_STAR
}

fun getSlot(color: Color): Int {
    when (color) {
        Color.RED -> return 10
        Color.BLUE -> return 11
        Color.GREEN -> return 12
        Color.FUCHSIA -> return 14
        Color.ORANGE -> return 15
        Color.YELLOW -> return 16
        Color.BLACK -> return 19
        Color.WHITE -> return 20
        Color.PURPLE -> return 21
        Color.GRAY -> return 23
        Color.AQUA -> return 24
        Color.LIME -> return 25
    }
    return 0
}
