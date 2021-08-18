package de.richtigeralex.amongus.task.classic.visual

import org.bukkit.Location

data class SubmitScanTask(override val taskName: String, override val locations: MutableList<Location>) : VisualTask {

    override val stages: Int = 1

    override fun solveTask() {
        TODO("Not yet implemented")
    }
}