import java.util.TimerTask

class IterTask(private val board: BoardInterface?) : TimerTask() {
    override fun run() {
        board?.nextGeneration()
        board?.printBoard()
        println()
    }
}