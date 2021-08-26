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