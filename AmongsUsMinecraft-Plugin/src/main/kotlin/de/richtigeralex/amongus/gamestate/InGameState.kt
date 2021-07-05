package de.richtigeralex.amongus.gamestate

import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import org.bukkit.Bukkit

class InGameState(val amongUsPlayerManager: AmongUsPlayerManager) : GameState() {

    override fun start() {
        Bukkit.broadcastMessage("Debug Ingame State started successfully")
    }

    override fun stop() {

    }
}