package de.richtigeralex.amongus.api.events

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * Is called when the Game is finished
 * You can't get the Winner out of the Event
 * @see ImposterWinningEvent or
 * @see CrewMateWinningEvent
 */
class GameEndingEvent : Event() {

    companion object {
        @JvmStatic
        var handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList
}

class ImposterWinningEvent : Event() {
    companion object {
        @JvmStatic
        var handlerList = HandlerList()
    }

    init {
        Bukkit.getPluginManager().callEvent(GameEndingEvent())
    }

    override fun getHandlers(): HandlerList = handlerList
}

class CrewMateWinningEvent : Event() {
    companion object {
        @JvmStatic
        var handlerList = HandlerList()
    }

    init {
        Bukkit.getPluginManager().callEvent(GameEndingEvent())
    }

    override fun getHandlers(): HandlerList = handlerList
}
