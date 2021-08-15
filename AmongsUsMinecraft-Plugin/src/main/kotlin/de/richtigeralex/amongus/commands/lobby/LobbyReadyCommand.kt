package de.richtigeralex.amongus.commands.lobby

import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.api.events.GameStartEvent
import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.gamestate.InGameState
import de.richtigeralex.amongus.map.VoteMapManager
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.ImposterPlayer
import de.richtigeralex.amongus.player.LobbyPlayer
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import java.util.function.Consumer

class LobbyReadyCommand(private val amongUsPlayerManager: AmongUsPlayerManager, private val voteMapManager: VoteMapManager) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        if (amongUsPlayerManager.gameStateManager.currentGameState is InGameState) return true

        val lobbyPlayer: LobbyPlayer = amongUsPlayerManager.lobbyPlayers.find { it.player == sender }!!
        lobbyPlayer.apply { isReady = !isReady }

        if (lobbyPlayer.isReady) Bukkit.broadcastMessage("§6${sender.name} §7ist nun ready")
        else Bukkit.broadcastMessage("§c${sender.name} $7ist nicht mehr ready")

        Bukkit.broadcastMessage("§e${amongUsPlayerManager.lobbyPlayers.filter { it.isReady }.size}§7/§6${amongUsPlayerManager.lobbyPlayers.size} §7sind ready")
        if (amongUsPlayerManager.lobbyPlayers.filter { it.isReady }.size == amongUsPlayerManager.lobbyPlayers.size) { // all players are ready
            Bukkit.broadcastMessage("§7Alle Spieler sind nun ready. Das Spiel startet")
            voteMapManager.scheduleMessageRepeater(false)
            var counter = 10

            amongUsPlayerManager.lobbyPlayers.forEach {
                it.player.inventory.clear()
                it.player.addPotionEffects(
                    mutableListOf(
                        PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 1, false, false),
                        PotionEffect(PotionEffectType.SLOW, 20 * 10, 255, false, false)
                    )
                )
            }

            Bukkit.getScheduler().scheduleSyncRepeatingTask(AmongUs.instance, Runnable {
                Bukkit.broadcastMessage("§e$counter")
                counter--
                if (counter == 0) {
                    Bukkit.broadcastMessage("§aDas Spielt startet jetzt!")
                    Bukkit.broadcastMessage("§aViel Spaß!")
                    amongUsPlayerManager.convertToInGameState() // start of game
                    amongUsPlayerManager.inGamePlayers.forEach {
                        it.player.sendMessage((it is ImposterPlayer).toString())
                        it.player.sendTitle(
                            if (it is ImposterPlayer) "§cImposter" else "§bCrewmate",
                            null, 10, 90, 20
                        )
                    }
                    amongUsPlayerManager.gameStateManager.setGameState(GameState.INGAME_STATE)
                    Bukkit.getPluginManager().callEvent(GameStartEvent())
                }
            }, 20L, 20L)
        }
        return true
    }
}