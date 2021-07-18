package de.richtigeralex.amongus.commands.general

import de.richtigeralex.amongus.map.IAmongUsMapManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CreateNewMapCommand(private val amongUsMapManager: IAmongUsMapManager) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        amongUsMapManager.createMap(args[0], sender as Player)
        sender.sendMessage("map created")
        return true
    }
}