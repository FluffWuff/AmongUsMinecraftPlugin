package de.richtigeralex.amongus.player.impostor

import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.CrewMatePlayer
import de.richtigeralex.amongus.player.ImposterPlayer
import de.richtigeralex.amongus.util.itembuilder.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack

class ImpostorWeapon(val amongUsPlayerManager: AmongUsPlayerManager) : Listener {

    private val weaponItemStack: ItemStack = ItemBuilder(material = Material.IRON_SWORD, displayName = "§cKill").build()

    @EventHandler
    fun handleCrewMateKill(event: EntityDamageByEntityEvent) {
        if(event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            event.isCancelled = true
            return
        }
        if(event.entity !is Player) {
            event.isCancelled = true
            return
        }
        if(event.damager !is Player) {
            event.isCancelled = true
            return
        }

        val entity: Player = event.entity as Player
        val damager: Player = event.damager as Player

        if(damager.inventory.itemInMainHand != weaponItemStack) {
            event.isCancelled = true
            return
        }

        val damagerImposterPlayer: ImposterPlayer = amongUsPlayerManager.inGamePlayers.find { it.player == damager } as ImposterPlayer
        val crewMatePlayer: CrewMatePlayer = amongUsPlayerManager.inGamePlayers.find { it.player == entity } as CrewMatePlayer
        damagerImposterPlayer.killCrewMate(crewMatePlayer)
    }

}