import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

class InfiniteBoard() : BoardInterface {

    private var board: HashMap<String, Cell> = hashMapOf() //empty hashmap
    var xMax: Int = 0   //track max and min for both x and y, so we can display the board
    var yMax: Int = 0
    var yMin: Int = 0
    var xMin: Int = 0

    override fun nextGeneration() {
        var c: Cell
        val killList: ArrayList<String> = arrayListOf() //list of hash values of cells to kill
        val birthList: ArrayList<String> = arrayListOf() //list of hash values of cells to birth
        val removeList: ArrayList<String> = arrayListOf() //list of hash values to remove from table (nothing next to them on board)

        //populate birthList and killList
        for(entry in board) {
            c = entry.value
            val hash: String = "${c.xPos} ${c.yPos}"
            val nearby: Int = numNearbyCells(c)

            if(c.value == 0 && nearby == 3) { //empty cell is birthed if exactly 3 living cells are near it
                birthList.add(hash)
            } else if(c.value != 0 && (nearby < 2 || nearby > 3)) { //cell with <2 neighbors or >3 neighbors dies
                killList.add(hash)
            } else if(nearby == 0) {
                removeList.add(hash)
            }
        }

        //now we have lists of what to kill, birth, and remove, so we can modify board accordingly
        for(i in removeList) {
            board.remove(i)
        }
        for(i in killList) {
            c = board.get(i)!!
            killCell(c.xPos, c.yPos, c.value)
        }
        for(i in birthList) {
            c = board.get(i)!!
            birthCell(c.xPos, c.yPos, 1)
        }

    }

    private fun numNearbyCells(c: Cell): Int { //return # of living cells next to c
        var sum = 0
        val x = c.xPos
        val y = c.yPos

        if(getCell(x - 1, y - 1) != 0) {        //up-left
            sum++
        }
        if(getCell(x, y - 1) != 0) {               //up
            sum++
        }
        if(getCell(x + 1, y - 1) != 0) {        //up-right
            sum++
        }
        if(getCell(x - 1, y) != 0) {               //left
            sum++
        }
        if(getCell(x + 1, y) != 0) {               //right
            sum++
        }
        if(getCell(x - 1, y + 1) != 0) {        //down-left
            sum++
        }
        if(getCell(x, y + 1) != 0) {               //down
            sum++
        }
        if(getCell(x + 1, y + 1) != 0) {        //down-right
            sum++
        }

        return sum
    }

    private fun populate(c: Cell) {
        val x = c.xPos
        val y = c.yPos
        var hash: String

        hash = "${x - 1} ${y - 1}"
        if(board.get(hash) == null) {                   //up-left
            birthCell(x - 1, y - 1, 0)
        }

        hash = "$x ${y - 1}"
        if(board.get(hash) == null) {                   //up
            birthCell(x, y - 1, 0)
        }

        hash = "${x + 1} ${y - 1}"
        if(board.get(hash) == null) {                   //up-right
            birthCell(x + 1, y - 1, 0)
        }

        hash = "${x - 1} $y"
        if(board.get(hash) == null) {                   //left
            birthCell(x - 1, y, 0)
        }

        hash = "${x + 1} $y"
        if(board.get(hash) == null) {                   //right
            birthCell(x + 1, y, 0)
        }

        hash = "${x - 1} ${y + 1}"
        if(board.get(hash) == null) {                   //down-left
            birthCell(x - 1, y + 1, 0)
        }

        hash = "$x ${y + 1}"
        if(board.get(hash) == null) {                   //down
            birthCell(x, y + 1, 0)
        }

        hash = "${x + 1} ${y + 1}"
        if(board.get(hash) == null) {                   //down-right
            birthCell(x + 1, y + 1, 0)
        }
    }

    override fun birthCell(x: Int, y: Int, player: Int): Boolean {
        val hash = "$x $y"
        var c: Cell? = board.get(hash)

        if(c == null) {
            c = Cell(player, x, y)
            board.put(hash, c)
        } else if(c.value == 0) {
            c.value = player
        } else {
            return false
        }

        if(c.value != 0) {
            populate(c)
        } //will create the cells adjacent to c

        if(x > xMax) {
            xMax = x
        }
        if(x < xMin) {
            xMin = x
        }
        if(y > yMax) {
            yMax = y
        }
        if(y < yMin) {
            yMin = y
        }

        return true
    }

    override fun getCell(x: Int, y: Int): Int {
        val hash: String = "$x $y"
        val c: Cell? = board.get(hash)

        if(c == null) {
            return 0
        } else {
            return c.value
        }
    }

    override fun killCell(x: Int, y: Int, player: Int): Boolean {
        if(getCell(x, y) != player) {
            return false
        } else {
            val hash = "$x $y"
            board.remove(hash)
            return true
        }
    }

    override fun printBoard() {
        for(y in yMin+1 until yMax) {
            for(x in xMin+1 until xMax) {
                val hash = "$x $y"
                val c = board.get(hash)

                if(c == null || c.value == 0) {
                    print(". ")
                } else {
                    print("${c.value} ")
                }
            }
            println()
        }
    }

    override fun save(saveFile: File): Boolean{
        val writer = BufferedWriter(FileWriter(saveFile))
        try {
            //write header
            writer.write("infinite   = true\n")
            writer.write("iterations = 0\n")
            writer.write("Width(x)   = " + (this.xMax - this.xMin).toString() + "\n")
            writer.write("Height(y)  = " + (this.yMax - this.yMin).toString() + "\n")
            //write board body
            for(y in yMin until yMax) {
                for(x in xMin until xMax) {
                    val hash = "$x $y"
                    val cell = board.get(hash)

                    when (cell) {
                        null -> writer.write("0 ")
                        else -> writer.write(cell.value.toString() + " ")
                    }
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

    private class Cell(v: Int, x: Int, y: Int) {
        var value: Int = v //Player that owns the cell
        var xPos: Int = x
        var yPos: Int = y
    }
}