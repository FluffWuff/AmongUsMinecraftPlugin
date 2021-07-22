package de.richtigeralex.amongus.player.impostor

import de.richtigeralex.amongus.api.events.ImposterKillEvent
import de.richtigeralex.amongus.api.events.ImposterWinningEvent
import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.CrewMatePlayer
import de.richtigeralex.amongus.player.ImposterPlayer
import de.richtigeralex.amongus.util.itembuilder.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack

class ImposterListener(val amongUsPlayerManager: AmongUsPlayerManager) : Listener {

    private val weaponItemStack: ItemStack = ItemBuilder(material = Material.IRON_SWORD, displayName = "§cKill").build()

    @EventHandler
    fun handleCrewMateKill(event: EntityDamageByEntityEvent) {
        event.isCancelled = true
        if(event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return
        if(event.entity !is Player) return
        if(event.damager !is Player) return

        val entity: Player = event.entity as Player
        val damager: Player = event.damager as Player

        if(damager.inventory.itemInMainHand != weaponItemStack) return

        val damagerImposterPlayer: ImposterPlayer = amongUsPlayerManager.inGamePlayers.find { it.player == damager } as ImposterPlayer
        val crewMatePlayer: CrewMatePlayer = amongUsPlayerManager.inGamePlayers.find { it.player == entity } as CrewMatePlayer
        if(damagerImposterPlayer.nextAvailableKill >= System.currentTimeMillis()) {
            damagerImposterPlayer.killCrewMate(crewMatePlayer)
            damagerImposterPlayer.nextAvailableKill = System.currentTimeMillis() + 30000L
        } else damager.sendMessage("§7Du musst insgesamt noch §c${(damagerImposterPlayer.nextAvailableKill - System.currentTimeMillis()) / 1000}s §7warten, um jemanden töten zu dürfen.")
    }

    @EventHandler
    fun handleImposterKill(event: ImposterKillEvent) {
        if(amongUsPlayerManager.inGamePlayers.filterIsInstance<ImposterPlayer>().filter { !it.isDead }.size == amongUsPlayerManager.inGamePlayers.filterIsInstance<CrewMatePlayer>().filter { !it.isDead }.size) {
            // Imposter wins the game
            Bukkit.getPluginManager().callEvent(ImposterWinningEvent())
            amongUsPlayerManager.gameStateManager.setGameState(GameState.LOBBY_STATE)
        }
    }

}
