package de.richtigeralex.amongus.map

import de.richtigeralex.amongus.player.LobbyPlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

class VoteMapManager(val amongUsMapManager: IAmongUsMapManager) {

    val voted: MutableMap<AmongUsMap, MutableList<LobbyPlayer>> = mutableMapOf()
    var timer: Timer? = null

    fun voteMap(lobbyPlayer: LobbyPlayer, mapName: String) {
        if(lobbyPlayer.hasVoted) {
            lobbyPlayer.player.sendMessage("§cDu hast bereits gevotet!")
            return
        }
        val votedMap: AmongUsMap? = amongUsMapManager.loadedMaps.find { it.name == mapName }
        if(votedMap == null) {
            lobbyPlayer.player.sendMessage("§cDie Map §e$mapName §cexistiert nicht!")
            return
        }

        if(voted.containsKey(votedMap))
            voted[votedMap]!!.add(lobbyPlayer)
        else
            voted[votedMap] = mutableListOf(lobbyPlayer)
        Bukkit.broadcastMessage("§e$mapName §7wurde von §6${lobbyPlayer.player.name} §7gevotet")
        lobbyPlayer.hasVoted = true
    }

    fun scheduleMessageRepeater(shouldStart: Boolean) {
        if(shouldStart) {
            timer = kotlin.concurrent.timer(period = 60_000L, initialDelay = 1_000L, action = {
                Bukkit.broadcastMessage("§7Momentaner Votestand:")
                voted.forEach { (t, u) ->
                    Bukkit.broadcastMessage("§e${t.name}§7: §6${u.size}")
                }
            })
        } else {
            timer!!.cancel()
        }
    }

}