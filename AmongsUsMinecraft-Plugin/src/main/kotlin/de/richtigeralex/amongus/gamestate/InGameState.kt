package de.richtigeralex.amongus.gamestate

import de.richtigeralex.amongus.map.IAmongUsMapManager
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import org.bukkit.Bukkit

class InGameState(val amongUsPlayerManager: AmongUsPlayerManager, val amongUsMapManager: IAmongUsMapManager) : GameState() {

    override fun start() {
        Bukkit.broadcastMessage("Debug Ingame State started successfully")

        amongUsPlayerManager.inGamePlayers.forEach {  }
    }

    override fun stop() {

    }
}