
class Board(private val sizeX: Int, private val sizeY: Int) : BoardInterface {

    private var board: Array<IntArray>

    init {
        board = Array(sizeX) { IntArray(sizeY) }
    }

    // Initialize array
    init {
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                board[i][j] = 0
            }
        }
    }

    constructor(sizeX: Int, sizeY: Int, initialState: Array<IntArray>) : this(sizeX, sizeY){
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                board[i][j] = initialState[i][j]
            }
        }
    }

    // Method to generate next game state
    override fun nextGeneration() {
        // This will be very performance inefficient for large boards. Maybe use a
        //  quad-linked list? each cell would use a reference to its left, right, up, down.
        // Should also use a Cell class
        val temp = Array(sizeX){IntArray(sizeY)}
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                temp[i][j] = this.check(i, j)
            }
        }
        board = temp
    }

    private fun check(i: Int, j: Int): Int {
        val n = this.numNeighbors(i, j)
        val cell = board[i][j]

        if (cell > 0 && (n <= 1 || n >= 4)) {
            return 0
        }else if (cell == 1 || (cell == 0 && n == 3)){
            return 1
        }

        return 0
    }

    override fun killCell(x: Int, y:Int, player:Int): Boolean{
        if(board[x][y] == player) {
            board[x][y] = 0
            return true
        } else {
            return false
        }
    }

    override fun birthCell(x:Int, y:Int, player:Int): Boolean{
        board[x][y] = player
        return true
    }

    override fun getCell(x:Int, y:Int):Int{
        return board[x][y]
    }

    private fun numNeighbors(x: Int, y: Int): Int {
        var n = 0

        /*
        0 0 0
        0 X 0
        0 0 0
        */
        val left = x == 0
        val right = x == sizeX - 1
        val top = y == 0
        val bottom = y == sizeY - 1

        if (!left) {
            if (!top && board[x - 1][y - 1] > 0)
                n++
            if (board[x - 1][y] > 0)
                n++
            if (!bottom && board[x - 1][y + 1] > 0)
                n++
        }
        if (!top && board[x][y - 1] > 0)
            n++
        if (!bottom && board[x][y + 1] > 0)
            n++
        if (!right) {
            if (!top && board[x + 1][y - 1] > 0) {
                n++
            }
            if (board[x + 1][y] > 0) {
                n++
            }
            if (!bottom && board[x + 1][y + 1] > 0) {
                n++
            }
        }

        return n
    }

    override fun printBoard() { //prints board to stdout
        for(i in 0 until this.sizeX) {
            for (j in 0 until this.sizeY) {
                val temp = this.board[i][j]
                print("$temp ")
            }
            println()
        }
        println()
    }
}