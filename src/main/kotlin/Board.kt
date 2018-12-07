import java.lang.Math.floorMod

class Board(private val sizeX: Int, private val sizeY: Int) : BoardInterface {

    private var board: Array<IntArray>
    private var toroidalMode = false

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

    constructor(sizeX: Int, sizeY: Int, toroidalMode: Boolean, initialState: Array<IntArray>) : this(sizeX, sizeY){
        this.toroidalMode = toroidalMode

        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                board[i][j] = initialState[i][j]
            }
        }
    }

    override fun isAlive(player: Int): Boolean {
        for (i in 0 until sizeX) {
            for (j in 0 until sizeY) {
                if (board[i][j] == player) {
                    return true
                }
            }
        }
        return false
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
        }else if (cell > 0 || (cell == 0 && n == 3)){
            return cell
        }

        return 0
    }

    override fun killCell(x: Int, y:Int, player:Int): Boolean{
        if (board[x][y] == player) {
            board[x][y] = 0
            return true
        } else {
            return false
        }
    }

    override fun birthCell(x:Int, y:Int, player:Int): Boolean{
        if (board[x][y] == 0) {
            board[x][y] = player
            return true
        } else  {
            return false
        }
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

        if(this.toroidalMode){
            //left
            if (board[floorMod((x-1), sizeX)][y] > 0)
                n++
            //right
            if (board[floorMod((x+1), sizeX)][y] > 0)
                n++
            //up
            if (board[x][floorMod((y-1), sizeY)] > 0)
                n++
            //down
            if (board[x][floorMod((y+1), sizeY)] > 0)
                n++
            //up left
            if (board[floorMod((x-1), sizeX)][floorMod((y-1), sizeY)] > 0)
                n++
            //down left
            if (board[floorMod((x-1), sizeX)][floorMod((y+1), sizeY)] > 0)
                n++
            //up right
            if (board[floorMod((x+1), sizeX)][floorMod((y-1), sizeY)] > 0)
                n++
            //down right
            if (board[floorMod((x+1), sizeX)][floorMod((y+1), sizeY)] > 0)
                n++
        }else {
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
        }

        return n
    }

    override fun printBoardLineHelpers() {
        for (i in -1 until this.sizeX) {
            for (j in -1 until this.sizeY) {
                if (i == -1 || j == -1) {
                    if (i == -1 && j == -1) {
                        print("+ ")
                    } else if (i == -1) {
                        print("$j ")
                    } else if (j == -1) {
                        print("$i ")
                    }
                } else {
                    val temp = this.board[i][j]
                    print("$temp ")
                }
            }
            println()
        }
        println()
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