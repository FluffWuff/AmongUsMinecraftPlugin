package de.richtigeralex.amongus.api.events

import de.richtigeralex.amongus.player.CrewMatePlayer
import de.richtigeralex.amongus.player.ImposterPlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ImposterKillEvent(val imposterPlayer: ImposterPlayer, val crewMatePlayer: CrewMatePlayer, val isCrewMateInTask: Boolean = false) : Event() {

    companion object {
        @JvmStatic
        var handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList
}