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
package de.richtigeralex.amongus.gamestate

import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.map.VoteMapManager
import de.richtigeralex.amongus.util.commands.UnregisterCommandUtil

class LobbyState(private val voteMapManager: VoteMapManager) : GameState() {

    override fun start() {
        voteMapManager.scheduleMessageRepeater(true)
    }

    override fun stop() {
        //UnregisterCommandUtil.unRegisterCommand(AmongUs.instance.getCommand("r")!!)
        //UnregisterCommandUtil.unRegisterCommand(AmongUs.instance.getCommand("voteMap")!!)
    }
}