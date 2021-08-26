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
package de.richtigeralex.amongus.util.commands

import de.richtigeralex.amongus.AmongUs
import org.bukkit.command.Command
import org.bukkit.command.PluginCommand
import org.bukkit.command.SimpleCommandMap
import java.lang.reflect.Field

object UnregisterCommandUtil {

    private fun getPrivateField(any: Any, field: String): Any {
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