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
package de.richtigeralex.amongus.player.impostor

import de.richtigeralex.amongus.api.events.ImposterKillEvent
import de.richtigeralex.amongus.api.events.ImposterWinningEvent
import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.gamestate.InGameState
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
            amongUsPlayerManager.gameStateManager.gameStates.filterIsInstance(InGameState::class.java).first().sequenceType = InGameState.AmongUsSequenceType.IMPOSTER_WIN
            amongUsPlayerManager.gameStateManager.setGameState(GameState.LOBBY_STATE)
        }
    }

}
