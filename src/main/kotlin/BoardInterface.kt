interface BoardInterface{

    fun nextGeneration()

    fun killCell(x: Int, y: Int, player: Int): Boolean

    fun birthCell(x: Int, y: Int, player: Int): Boolean

    fun getCell(x: Int, y: Int): Int

    fun printBoard()

}