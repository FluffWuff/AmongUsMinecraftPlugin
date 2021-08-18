package de.richtigeralex.amongus.task.classic.long

import org.bukkit.Location

data class InspectSampleTask(override val taskName: String, override val locations: MutableList<Location>) : LongTask {

    override val stages: Int = 1

    override fun solveTask() {
        TODO("Not yet implemented")
    }
}