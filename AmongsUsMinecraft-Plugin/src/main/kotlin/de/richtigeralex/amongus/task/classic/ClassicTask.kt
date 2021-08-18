package de.richtigeralex.amongus.task.classic

import de.richtigeralex.amongus.task.AmongUsTask
import org.bukkit.Location

interface ClassicTask : AmongUsTask {

    override val taskName: String
    override val locations: MutableList<Location>
    override fun solveTask()

    /**
     * an indicator how many stages a task has, reference: https://among-us.fandom.com/wiki/Tasks
     *
     * if stages = 1 ==> task has only one stage to complete e.g. SwipeCardTask
     *
     * stages = 3 ==> task has two stages to complete e.g. FixWiringTask
     *
     * getting the stage where the CrewMate is currently see AmongUsTaskManager
     */
    val stages: Int

}