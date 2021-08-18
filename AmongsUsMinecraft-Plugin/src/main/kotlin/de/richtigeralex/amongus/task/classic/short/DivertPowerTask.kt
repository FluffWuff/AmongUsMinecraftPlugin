package de.richtigeralex.amongus.task.classic.short

import org.bukkit.Location

data class DivertPowerTask(override val taskName: String, override val locations: MutableList<Location>) : ShortTask {

    override val stages: Int = 1

    override fun solveTask() {
        TODO("Not yet implemented")
    }

}