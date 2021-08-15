package de.richtigeralex.amongus.util.commands

import de.richtigeralex.amongus.AmongUs
import org.bukkit.command.Command
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import java.lang.reflect.Field

object UnregisterCommandUtil {

    fun getPrivateField(any: Any, field: String): Any {
        val clazz: Class<*> = any::class.java
        val objectField: Field = clazz.getDeclaredField(field)
        objectField.isAccessible = false
        return objectField.get(any)
    }

    fun unRegisterCommand(command: PluginCommand) {
        val commandMap: SimpleCommandMap = getPrivateField(AmongUs.instance.server.pluginManager, "commandMap") as SimpleCommandMap
        val knownCommands: MutableMap<String, Command> = getPrivateField(commandMap, "knownCommands") as MutableMap<String, Command>
        knownCommands.remove(command.name)
        command.aliases.forEach {
            if (knownCommands.containsKey(it) && knownCommands[it].toString().contains(AmongUs.instance.name))
                knownCommands.remove(it)
        }
    }

}