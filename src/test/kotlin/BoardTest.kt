import org.junit.jupiter.api.Assertions

internal class BoardTest {

    //test that all cells are 0 when initialized
    @org.junit.jupiter.api.Test
    fun initTest() {
        val board = Board(4,4)
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                Assertions.assertEquals(board.getCell(i, j), 0)
            }
        }
    }

    //test that cell is first alive then dead
    @org.junit.jupiter.api.Test
    fun killCellTest() {
        val board = Board(4,4)
        board.birthCell(3,3)
        Assertions.assertEquals(board.getCell(3, 3), 1)
        board.killCell(3,3)
        Assertions.assertEquals(board.getCell(3, 3), 0)
    }

    //test the birth cell function
    @org.junit.jupiter.api.Test
    fun birthCellTest() {
        val board = Board(4,4)
        board.birthCell(1,1)
        Assertions.assertEquals(board.getCell(1, 1), 1)
    }

    //check that if a cell has 3 neighbors it is birthed by iterate
    @org.junit.jupiter.api.Test
    fun iterateTest() {
        val board = Board(4,4)
        board.birthCell(1,2)
        board.birthCell(0,1)
        board.birthCell(1,1)
        board.nextGeneration()
        Assertions.assertEquals(board.getCell(0, 2), 1)
    }

}
