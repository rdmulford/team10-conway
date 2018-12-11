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
        board.birthCell(3,3, 1)
        Assertions.assertEquals(board.getCell(3, 3), 1)
        board.killCell(3,3, 1)
        Assertions.assertEquals(board.getCell(3, 3), 0)
    }

    //test the birth cell function
    @org.junit.jupiter.api.Test
    fun birthCellTest() {
        val board = Board(4,4)
        board.birthCell(1,1, 1)
        Assertions.assertEquals(board.getCell(1, 1), 1)
    }

    //check that if a cell has 3 neighbors it is birthed by iterate
    @org.junit.jupiter.api.Test
    fun iterateTest() {
        val board = Board(4, 4)
        board.birthCell(1, 2, 1)
        board.birthCell(0, 1, 1)
        board.birthCell(1, 1, 1)
        board.nextGeneration()
        Assertions.assertEquals(board.getCell(0, 2), 1)
    }

    //a non-toroidal glider
    @org.junit.jupiter.api.Test
    fun gliderTest() {
        val board = Board(20, 20)
        board.birthCell(1, 3, 1)
        board.birthCell(2, 3, 1)
        board.birthCell(3, 3, 1)
        board.birthCell(3, 2, 1)
        board.birthCell(2, 1, 1)
        //original state of the board
        /* * * * * *
           * * * 1 *
           * 1 * 1 *
           * * 1 1 *
           * * * * * */

        //every fourth iteration the glider should return to its original form
        //and should shift one x and one y value
        for (i in 0 until 4) {
            board.nextGeneration()
        }
        Assertions.assertEquals(board.getCell(2, 4), 1)
        Assertions.assertEquals(board.getCell(3, 4), 1)
        Assertions.assertEquals(board.getCell(4, 4), 1)
        Assertions.assertEquals(board.getCell(4, 3), 1)
        Assertions.assertEquals(board.getCell(3, 2), 1)

        //check 11 more shifts
        for (i in 0 until 44) {
            board.nextGeneration()
        }
        Assertions.assertEquals(board.getCell(13, 15), 1)
        Assertions.assertEquals(board.getCell(14, 15), 1)
        Assertions.assertEquals(board.getCell(15, 15), 1)
        Assertions.assertEquals(board.getCell(15, 14), 1)
        Assertions.assertEquals(board.getCell(14, 13), 1)
    }

    //test a toroidal glider
    @org.junit.jupiter.api.Test
    fun toroidalGliderTest() {
        val x = 20
        val y = 20
        val temp = Array(x){IntArray(y)}
        for (i in 0 until x) {
            for (j in 0 until y) {
                temp[i][j] = 0
            }
        }
        temp[1][3] = 1
        temp[2][3] = 1
        temp[3][3] = 1
        temp[3][2] = 1
        temp[2][1] = 1
        val board = Board(15, 15, true, temp, "Singleplayer")
        //original state of the board
        /* * * * * *
           * * * 1 *
           * 1 * 1 *
           * * 1 1 *
           * * * * * */
        //after 60 iterations the glider should be reset
        for (i in 0 until 60) {
            board.nextGeneration()
        }
        Assertions.assertEquals(board.getCell(1, 3), 1)
        Assertions.assertEquals(board.getCell(2, 3), 1)
        Assertions.assertEquals(board.getCell(3, 3), 1)
        Assertions.assertEquals(board.getCell(3, 2), 1)
        Assertions.assertEquals(board.getCell(2, 1), 1)
    }

    //test the block still life
    @org.junit.jupiter.api.Test
    fun blockStillLifeTest() {
        val board = Board(4, 4)
        board.birthCell(1, 1, 1)
        board.birthCell(1, 2, 1)
        board.birthCell(2, 1, 1)
        board.birthCell(2, 2, 1)

        //iterate a random number of times and check the block stayed still
        val randNum = (1..50).shuffled().first()
        for (i in 0 until randNum) {
            board.nextGeneration()
        }
        Assertions.assertEquals(board.getCell(1, 1), 1)
        Assertions.assertEquals(board.getCell(1, 2), 1)
        Assertions.assertEquals(board.getCell(2, 1), 1)
        Assertions.assertEquals(board.getCell(2, 2), 1)
    }

    //test the block still life
    @org.junit.jupiter.api.Test
    fun beehiveStillLifeTest() {
        val board = Board(5, 6)
        board.birthCell(2, 1, 1)
        board.birthCell(1, 2, 1)
        board.birthCell(3, 2, 1)
        board.birthCell(1, 3, 1)
        board.birthCell(3, 3, 1)
        board.birthCell(2, 4, 1)

        //iterate a random number of times and check the beehive stayed still
        val randNum = (1..50).shuffled().first()
        for (i in 0 until randNum) {
            board.nextGeneration()
        }
        Assertions.assertEquals(board.getCell(2, 1), 1)
        Assertions.assertEquals(board.getCell(1, 2), 1)
        Assertions.assertEquals(board.getCell(3, 2), 1)
        Assertions.assertEquals(board.getCell(1, 3), 1)
        Assertions.assertEquals(board.getCell(3, 3), 1)
        Assertions.assertEquals(board.getCell(2, 4), 1)
    }

    //test the Gosper glider
    @org.junit.jupiter.api.Test
    fun gliderGunTest() {
        val board = Board(15, 38)
        board.birthCell(5, 1, 1)
        board.birthCell(6, 1, 1)
        board.birthCell(5, 2, 1)
        board.birthCell(6, 2, 1)
        board.birthCell(5, 11, 1)
        board.birthCell(6, 11, 1)
        board.birthCell(7, 11, 1)
        board.birthCell(4, 12, 1)
        board.birthCell(8, 12, 1)
        board.birthCell(3, 13, 1)
        board.birthCell(9, 13, 1)
        board.birthCell(3, 14, 1)
        board.birthCell(9, 14, 1)
        board.birthCell(6, 15, 1)
        board.birthCell(4, 16, 1)
        board.birthCell(8, 16, 1)
        board.birthCell(5, 17, 1)
        board.birthCell(6, 17, 1)
        board.birthCell(7, 17, 1)
        board.birthCell(6, 18, 1)
        board.birthCell(3, 21, 1)
        board.birthCell(4, 21, 1)
        board.birthCell(5, 21, 1)
        board.birthCell(3, 22, 1)
        board.birthCell(4, 22, 1)
        board.birthCell(5, 22, 1)
        board.birthCell(2, 23, 1)
        board.birthCell(6, 23, 1)
        board.birthCell(1, 25, 1)
        board.birthCell(2, 25, 1)
        board.birthCell(6, 25, 1)
        board.birthCell(7, 25, 1)
        board.birthCell(3, 35, 1)
        board.birthCell(4, 35, 1)
        board.birthCell(3, 36, 1)
        board.birthCell(4, 36, 1)

        //the first glider should be created after the 15th generation
        for (i in 0 until 15) {
            board.nextGeneration()
        }

        //check to see if the glider has been created
        Assertions.assertEquals(board.getCell(6, 21), 1)
        Assertions.assertEquals(board.getCell(7, 22), 1)
        Assertions.assertEquals(board.getCell(8, 20), 1)
        Assertions.assertEquals(board.getCell(8, 21), 1)
        Assertions.assertEquals(board.getCell(8, 22), 1)

        //the second glider should be created after 30 more iterations
        for (i in 0 until 30) {
            board.nextGeneration()
        }

        //check to see if the glider has been created
        Assertions.assertEquals(board.getCell(6, 21), 1)
        Assertions.assertEquals(board.getCell(7, 22), 1)
        Assertions.assertEquals(board.getCell(8, 20), 1)
        Assertions.assertEquals(board.getCell(8, 21), 1)
        Assertions.assertEquals(board.getCell(8, 22), 1)

        //a glider should be created every 30 iterations now
        for (i in 0 until 30) {
            board.nextGeneration()
        }

        //check to see if the glider has been created
        Assertions.assertEquals(board.getCell(6, 21), 1)
        Assertions.assertEquals(board.getCell(7, 22), 1)
        Assertions.assertEquals(board.getCell(8, 20), 1)
        Assertions.assertEquals(board.getCell(8, 21), 1)
        Assertions.assertEquals(board.getCell(8, 22), 1)

        //15 more iterations should not have a new glider
        for (i in 0 until 15) {
            board.nextGeneration()
        }
        //check to see if the glider has not been created
        Assertions.assertNotEquals(board.getCell(6, 21), 1)
        Assertions.assertNotEquals(board.getCell(7, 22), 1)
        Assertions.assertNotEquals(board.getCell(8, 20), 1)
        Assertions.assertNotEquals(board.getCell(8, 21), 1)
        Assertions.assertNotEquals(board.getCell(8, 22), 1)
    }
}
