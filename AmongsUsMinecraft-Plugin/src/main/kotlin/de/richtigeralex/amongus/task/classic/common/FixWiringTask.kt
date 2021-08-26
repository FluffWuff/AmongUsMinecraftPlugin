package de.richtigeralex.amongus.task.classic.common

import com.gmail.filoghost.holographicdisplays.api.Hologram
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI
import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.player.AmongUsPlayer
import de.richtigeralex.amongus.player.CrewMatePlayer
import de.richtigeralex.amongus.task.AmongUsTaskManager
import org.bukkit.Location

data class FixWiringTask(override val taskName: String, override val locations: MutableList<Location>, override val amongUsPlayer: CrewMatePlayer, override val taskManager: AmongUsTaskManager) : CommonTask {

    override val stages: Int = 3
    override var currentStage: Int = 1
    override var hologram: Hologram = HologramsAPI.createHologram(AmongUs.instance, locations[currentStage - 1])

    override var isFinished: Boolean = false

    override fun solveTask() {

    }

}