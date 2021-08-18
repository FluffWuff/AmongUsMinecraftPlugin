package de.richtigeralex.amongus.task.classic.common

import org.bukkit.Location

data class FIxWiringTask(override val taskName: String, override val locations: MutableList<Location>) : CommonTask {

    override val stages: Int = 3

    override fun solveTask() {

    }

}