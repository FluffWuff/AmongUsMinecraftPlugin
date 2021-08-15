package de.richtigeralex.amongus.api.events

import de.richtigeralex.amongus.AmongUs
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class GameStartEvent : Event() {

    companion object {
        @JvmStatic
        var handlerList = HandlerList()
    }

    init {
        Bukkit.getScheduler().cancelTasks(AmongUs.instance)
    }

    override fun getHandlers(): HandlerList = handlerList

}