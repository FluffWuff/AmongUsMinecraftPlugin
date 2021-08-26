package de.richtigeralex.amongus.commands.lobby

import de.richtigeralex.amongus.map.VoteMapManager
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class VoteMapCommand(val voteMapManager: VoteMapManager, val amongUsPlayerManager: AmongUsPlayerManager) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(args.isEmpty()) {
            sender.sendMessage("§cDu musst eine §eMap §cangeben!")
            return true
        }
        amongUsPlayerManager.lobbyPlayers.find { it.player == sender as Player }!!.let { voteMapManager.voteMap(it, args[0]) }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String> {
        val completions: MutableList<String> = mutableListOf()
        val completeExamples: MutableList<String> = mutableListOf()
        voteMapManager.amongUsMapManager.loadedMaps.forEach { completeExamples.add(it.name) }
        StringUtil.copyPartialMatches(args[0], completeExamples, completions)
        completions.sort()
        return completions
    }
}