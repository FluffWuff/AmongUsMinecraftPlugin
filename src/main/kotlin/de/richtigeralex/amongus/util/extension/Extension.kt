package de.richtigeralex.amongus.util.extension

import de.richtigeralex.amongus.player.LobbyPlayer
import de.richtigeralex.amongus.util.itembuilder.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

fun Player.setUpLobby(lobbyPlayer: LobbyPlayer) {
    this.inventory.helmet = ItemBuilder(material = Material.LEATHER_HELMET, color = lobbyPlayer.color).build()
    this.inventory.chestplate = ItemBuilder(material = Material.LEATHER_CHESTPLATE, color = lobbyPlayer.color).build()
    this.inventory.leggings = ItemBuilder(material = Material.LEATHER_LEGGINGS, color = lobbyPlayer.color).build()
    this.inventory.boots = ItemBuilder(material = Material.LEATHER_BOOTS, color = lobbyPlayer.color).build()

    this.inventory.setItem(4, ItemBuilder(material = Material.NETHER_STAR, displayName = "§5Farbauswahl").build())
}

fun Player.setUpSpectator() {

}