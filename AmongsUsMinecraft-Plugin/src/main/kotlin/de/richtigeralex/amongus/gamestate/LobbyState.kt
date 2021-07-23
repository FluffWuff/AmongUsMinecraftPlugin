package de.richtigeralex.amongus.gamestate

import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.map.VoteMapManager
import de.richtigeralex.amongus.util.commands.UnregisterCommandUtil

class LobbyState(private val voteMapManager: VoteMapManager) : GameState() {

    override fun start() {
        voteMapManager.scheduleMessageRepeater(true)
    }

    override fun stop() {
        voteMapManager.scheduleMessageRepeater(false)

        UnregisterCommandUtil.unRegisterCommand(AmongUs.instance.getCommand("r")!!)
        UnregisterCommandUtil.unRegisterCommand(AmongUs.instance.getCommand("voteMap")!!)
    }
}