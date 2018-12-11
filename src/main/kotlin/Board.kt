import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.Math.floorMod

class Board(private val sizeX: Int, private val sizeY: Int) : BoardInterface {

    private var board: Array<IntArray>
    private var toroidalMode = false
    private var mode = "Singleplayer"

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

    constructor(sizeX: Int, sizeY: Int, toroidalMode: Boolean, initialState: Array<IntArray>, mode: String) : this(sizeX, sizeY){
        this.toroidalMode = toroidalMode
        this.mode = mode

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
        val neighbors = intArrayOf(0,0,0,0,0,0)
        val n = this.numNeighbors(i, j, neighbors)
        val cell = board[i][j]

        var max = 0
        var max_index = 0
        for(i in 0 until 6) {
            if(neighbors[i] > max) {
                max = neighbors[i]
                max_index = i
            } else if (neighbors[i] == max) {
                max_index = 5
            }
        }

        if (cell > 0 && (n <= 1 || n >= 4)) {
            return 0
        } else if (cell > 0 && (n == 3 || n == 2)) {
            return cell
        } else if (cell == 0 && n == 3) {
            if(mode.compareTo("Singleplayer") == 0) {
                return 1
            } else {
                return max_index
            }
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

    private fun numNeighbors(x: Int, y: Int, neighbors: IntArray): Int {
        var n = 0
        /*
        0 0 0
        0 X 0
        0 0 0
        */

        if(this.toroidalMode){
            //left
            var cellVal = board[floorMod((x-1), sizeX)][y]
            if (cellVal > 0) {
                n++
                neighbors[cellVal]++
            }
            //right
            cellVal = board[floorMod((x+1), sizeX)][y]
            if (cellVal > 0) {
                n++
                neighbors[cellVal]++
            }
            //up
            cellVal = board[x][floorMod((y-1), sizeY)]
            if (cellVal > 0) {
                n++
                neighbors[cellVal]++
            }
            //down
            cellVal = board[x][floorMod((y+1), sizeY)]
            if (cellVal > 0) {
                n++
                neighbors[cellVal]++
            }
            //up left
            cellVal = board[floorMod((x-1), sizeX)][floorMod((y-1), sizeY)]
            if (cellVal > 0) {
                n++
                neighbors[cellVal]++
            }
            //down left
            cellVal = board[floorMod((x-1), sizeX)][floorMod((y+1), sizeY)]
            if (cellVal > 0) {
                n++
                neighbors[cellVal]++
            }
            //up right
            cellVal = board[floorMod((x+1), sizeX)][floorMod((y-1), sizeY)]
            if (cellVal > 0) {
                n++
                neighbors[cellVal]++
            }
            //down right
            cellVal = board[floorMod((x+1), sizeX)][floorMod((y+1), sizeY)]
            if (cellVal > 0) {
                n++
                neighbors[cellVal]++
            }
        }else {
            val left = x == 0
            val right = x == sizeX - 1
            val top = y == 0
            val bottom = y == sizeY - 1

            if (!left) {
                if (!top && board[x - 1][y - 1] > 0) {
                    n++
                    neighbors[board[x - 1][y - 1]]++
                }
                if (board[x - 1][y] > 0) {
                    n++
                    neighbors[board[x - 1][y]]++
                }
                if (!bottom && board[x - 1][y + 1] > 0) {
                    n++
                    neighbors[board[x - 1][y + 1]]++
                }
            }
            if (!top && board[x][y - 1] > 0) {
                n++
                neighbors[board[x][y - 1]]++
            }
            if (!bottom && board[x][y + 1] > 0) {
                n++
                neighbors[board[x][y + 1]]++
            }
            if (!right) {
                if (!top && board[x + 1][y - 1] > 0) {
                    n++
                    neighbors[board[x + 1][y - 1]]++
                }
                if (board[x + 1][y] > 0) {
                    n++
                    neighbors[board[x + 1][y]]++
                }
                if (!bottom && board[x + 1][y + 1] > 0) {
                    n++
                    neighbors[board[x + 1][y + 1]]++
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
                        print("  + ")
                    } else if (i == -1) {
                        print("$j ".padStart(3))
                    } else if (j == -1) {
                        print("$i".padStart(3))
                    }
                } else {
                    val temp = this.board[i][j]
                    print("$temp".padStart(3))
                }
            }
            println()
        }
        println()
    }

    override fun save(saveFile: File): Boolean{
        val writer = BufferedWriter(FileWriter(saveFile))
        try {
            //write header
            writer.write("infinite   = false\n")
            writer.write("iterations = 0\n")
            writer.write("toroidal   = " + this.toroidalMode.toString() + "\n")
            writer.write("Width(x)   = " + this.sizeX.toString() + "\n")
            writer.write("Height(y)  = " + this.sizeY.toString() + "\n")
            //write board body
            for (i in 0 until this.sizeY) {
                for (j in 0 until this.sizeX) {
                    val temp = this.board[i][j]
                    writer.write(temp.toString() + " ")
                }
                writer.write("\n")
            }
            writer.close()
            return true
        }catch(e: IOException){
            println("Error: File could not be saved")
            return false
        }
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