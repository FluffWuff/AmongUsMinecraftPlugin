package de.richtigeralex.amongus.task.classic.common

import org.bukkit.Location

data class SwipeCardTask(override val taskName: String, override val locations: MutableList<Location>) : CommonTask {

    override val stages: Int = 0

    override fun solveTask() {

    }

}