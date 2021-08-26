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
package de.richtigeralex.amongus

import de.richtigeralex.amongus.commands.general.CreateNewMapCommand
import de.richtigeralex.amongus.commands.general.TaskLocationCommand
import de.richtigeralex.amongus.commands.lobby.LobbyReadyCommand
import de.richtigeralex.amongus.commands.lobby.VoteMapCommand
import de.richtigeralex.amongus.gamestate.GameState
import de.richtigeralex.amongus.gamestate.GameStateManager
import de.richtigeralex.amongus.gamestate.InGameState
import de.richtigeralex.amongus.gamestate.LobbyState
import de.richtigeralex.amongus.listener.CancelUselessListener
import de.richtigeralex.amongus.listener.PlayerHandleListener
import de.richtigeralex.amongus.map.AmongUsMap
import de.richtigeralex.amongus.map.IAmongUsMapManager
import de.richtigeralex.amongus.map.VoteMapManager
import de.richtigeralex.amongus.map.yaml.YamlAmongUsMapManager
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.impostor.ImposterListener
import de.richtigeralex.amongus.task.AmongUsTaskManager
import de.richtigeralex.amongus.util.itembuilder.ItemBuilderManager
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class AmongUs : JavaPlugin() {

    companion object {
        @JvmStatic
        lateinit var instance: AmongUs
    }

    private val amongUsMapManager: IAmongUsMapManager

    init {
        instance = this

        val serializableClasses = arrayOf(AmongUsMap::class.java) // more classes in future
        Arrays.stream(serializableClasses).forEach(ConfigurationSerialization::registerClass)
        amongUsMapManager = YamlAmongUsMapManager()
    }

    override fun onEnable() {
        amongUsMapManager.loadMaps()
        println("Loaded all maps")

        val voteMapManager = VoteMapManager(amongUsMapManager)
        val gameStateManager = GameStateManager(this)
        val amongUsPlayerManager = AmongUsPlayerManager(gameStateManager)

        gameStateManager.gameStates[GameState.LOBBY_STATE] = LobbyState(voteMapManager)
        gameStateManager.gameStates[GameState.INGAME_STATE] = InGameState(amongUsPlayerManager, amongUsMapManager)

        gameStateManager.setGameState(GameState.LOBBY_STATE)
        println("started Lobby State")

        Bukkit.getPluginManager().registerEvents(ItemBuilderManager.ItemBuilderListener(), this)
        Bukkit.getPluginManager().registerEvents(CancelUselessListener(), this)
        Bukkit.getPluginManager().registerEvents(ImposterListener(amongUsPlayerManager), this)
        Bukkit.getPluginManager().registerEvents(PlayerHandleListener(gameStateManager, amongUsPlayerManager, voteMapManager), this)

        this.getCommand("createMap")!!.setExecutor(CreateNewMapCommand(amongUsMapManager))
        this.getCommand("r")!!.setExecutor(LobbyReadyCommand(amongUsPlayerManager, voteMapManager))
        val voteMapCommand = VoteMapCommand(voteMapManager, amongUsPlayerManager)
        this.getCommand("voteMap")!!.setExecutor(voteMapCommand)
        this.getCommand("voteMap")!!.tabCompleter = voteMapCommand
        this.getCommand("taskLocation")!!.setExecutor(TaskLocationCommand(amongUsMapManager))
    }

    override fun onDisable() {
        amongUsMapManager.saveAllMaps()
    }

}