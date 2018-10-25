class Board(private val sizeX: Int, private val sizeY: Int) {

    private val board: Array<IntArray>

    init {
        board = Array(sizeX) { IntArray(sizeY) }
    }

    // Initialize array
    fun init() {
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                board[i][j] = 0
            }
        }
    }

    fun killCell(x: Int, y: Int) {
        board[x][y] = 0
    }

    fun birthCell(x: Int, y: Int) {
        board[x][y] = 1
    }


    // Method to iterate to next state of game
    fun iterate() {
        // TODO:
        // This will be very performance inefficient for large boards. Maybe use a
        //  quad-linked list? each cell would use a reference to its left, right, up, down.
        // Should also use a Cell class
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                this.check(i, j)
            }
        }
    }

    private fun check(i: Int, j: Int) {
        val n = this.numNeighbors(i, j)
        val cell = board[i][j]

        if (cell > 0 && (n <= 1 || n >= 4)) {
            killCell(i, j)
        } else if (n == 3) {
            birthCell(i, j)
        }
    }

    private fun numNeighbors(x: Int, y: Int): Int {
        var n = 0

        /*
        0 0 0
        0 X 0
        0 0 0
        */
        //TODO yeah this is a bit convoluted... need a better data struct and Cell class
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
            if (!top && board[x + 1][y - 1] > 0)
                n++
            if (board[x + 1][y] > 0)
                n++
            if (!bottom && board[x + 1][y + 1] > 0)
                n++
        }

        return n
    }

    fun getCell(x: Int, y: Int) : Int {
        return board[x][y]
    }

    fun printBoard() {
        for(i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                print(board[i][j])
            }
            println()
        }
        println()
    }
}
