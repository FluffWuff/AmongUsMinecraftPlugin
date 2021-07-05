package de.richtigeralex.amongus.commands.lobby

import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.gamestate.InGameState
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.ImposterPlayer
import de.richtigeralex.amongus.player.LobbyPlayer
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.concurrent.timer

class LobbyReadyCommand(private val amongUsPlayerManager: AmongUsPlayerManager) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return true
        if(amongUsPlayerManager.gameStateManager.currentGameState is InGameState) return true

        val lobbyPlayer: LobbyPlayer = amongUsPlayerManager.lobbyPlayers.find { it.player == sender}!!
        lobbyPlayer.apply { isReady = !isReady }

        if(lobbyPlayer.isReady) Bukkit.broadcastMessage("§6${sender.name} §7ist nun ready")
        else Bukkit.broadcastMessage("§c${sender.name} $7ist nicht mehr ready")

        Bukkit.broadcastMessage("§e${amongUsPlayerManager.lobbyPlayers.filter { it.isReady }.size}§7/§6${amongUsPlayerManager.lobbyPlayers.size} §7sind ready")
        if(amongUsPlayerManager.lobbyPlayers.filter { it.isReady }.size == amongUsPlayerManager.lobbyPlayers.size) { // all players are ready
            Bukkit.broadcastMessage("§7Alle Spieler sind nun ready. Das Spiel startet")
            var counter = 10
            kotlin.concurrent.timer(period = 1_000L, initialDelay = 1_000L, action = {
                Bukkit.broadcastMessage("§e$counter")
                counter--
                if(counter == 0) {
                    Bukkit.broadcastMessage("§aDas Spielt startet jetzt!")
                    Bukkit.broadcastMessage("§aViel Spaß!")
                    amongUsPlayerManager.convertToInGameState() // start of game
                    amongUsPlayerManager.inGamePlayers.forEach { it.player.sendMessage((it is ImposterPlayer).toString()) }
                    amongUsPlayerManager.gameStateManager.setGameState(GameState.INGAME_STATE)
                    this.cancel()
                }
            })
        }
        return true
    }
}