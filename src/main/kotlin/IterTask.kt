import java.util.TimerTask

class IterTask(private val board: Board?) : TimerTask() {
    override fun run() {
        board?.nextGeneration()
        board?.printBoard()
    }
}