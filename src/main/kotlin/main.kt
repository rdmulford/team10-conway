import java.io.File
import java.io.BufferedReader
import java.io.IOException
import java.util.*

fun main(args : Array<String>) {
    var inputString: String
    var iterations = 0
    var board: BoardInterface?
    var multiplayer: Boolean

    //initializations
    multiplayer = false
    board = null

    println("Team 10: Conway's Game of Life Started")

    do {
        println("What kind of game is this?\nsingleplayer or multiplayer: ")
        inputString = readLine()!!
        if (inputString.compareTo("singleplayer") == 0){
            multiplayer = false
        }else if (inputString.compareTo("multiplayer") == 0){
            multiplayer = true
        }
    }while((inputString.compareTo("singleplayer") != 0) and (inputString.compareTo("multiplayer") != 0))

    //determine how we are getting our board
    do {

        print("Load saved board or create new board? (load / new): ")

        inputString = readLine()!!

    } while((inputString.compareTo("load") != 0) and (inputString.compareTo("new") != 0))

    //new board
    if (inputString.compareTo("new") == 0){
        println("NOT IMPLEMENTED")
        return
    }

    //load board
    if (inputString.compareTo("load") == 0) {
        if (multiplayer){
            println("NOT IMPLEMENTED")
            return
        }else {
            board = loadBoard()
        }
    }

    //check for successful board creation
    if (board != null) {
        println("Initial Board:")
        board.printBoard()
    }else{
        println("ERROR: board generation or load has failed")
        return
    }

    if (! multiplayer) {
        //single player game
        do {
            print("Iteration[$iterations]\nnext (n), quit (q), iterate (i), save (s), play (p): ")
            inputString = readLine()!!
            println()
            when {
                (inputString.compareTo("play") == 0 || inputString.compareTo("p") == 0) -> {
                    //iterations per second
                    var valid: Boolean
                    var iterationsPerSecond = 0.0
                    do {
                        valid = true
                        print("Number of iterations per second: ")
                        inputString = readLine()!!
                        try {
                            iterationsPerSecond = inputString.toDouble()
                            if(iterationsPerSecond <= 0){
                                println("Invalid Input: Must be positive nonzero number")
                                valid = false
                            }
                        } catch (e: NumberFormatException) {
                            println("Invalid Input: Must be valid number")
                            valid = false
                        }
                    } while(!valid)
                    intervalIterate(iterationsPerSecond, board)
                }
                (inputString.compareTo("save") == 0 || inputString.compareTo("s") == 0) -> {
                    println("Saving board")
                    var valid1: Boolean
                    var valid2:Boolean
                    var file: File
                    do{
                        valid1 = true
                        print("Enter file name: ")
                        inputString = readLine()!!
                        file = File("boards/$inputString")
                        if (file.exists()){ //file exists, make sure user wants to save over it
                            do {
                                valid2 = true
                                print("File already exists. Overwrite?(y/n) ")
                                inputString = readLine()!!
                                if (inputString.compareTo("y") == 0) {
                                    valid1 = true
                                }else if (inputString.compareTo("n") == 0) {
                                    valid1 = false
                                }else{
                                    valid2 = false
                                }
                            } while(!valid2)
                        }else{ //file does not exist, try to make it
                            try{
                                file.createNewFile()
                            }catch(e: IOException){
                                println("Error: File could not be created. Try Again")
                                valid1 = false
                            }
                        }
                    } while(!valid1)
                    board.save(file)
                }
                (inputString.compareTo("next") == 0 || inputString.compareTo("n") == 0) -> {
                    board.nextGeneration()
                    board.printBoard()
                    iterations += 1
                }
                (inputString.compareTo("quit") == 0 || inputString.compareTo("q") == 0) -> {
                    println("Quitting, thanks for running our awesome program")
                }
                (inputString.compareTo("iterate") == 0 || inputString.compareTo("i") == 0) -> {
                    var iterationAmount = 0
                    var valid: Boolean
                    do {
                        valid = true
                        print("How many iterations?: ")
                        inputString = readLine()!!
                        try {
                            iterationAmount = inputString.toInt()
                            if (iterationAmount < 1) {
                                println("Invalid Input: Must be positive nonzero number")
                                valid = false
                            }
                        } catch (e: NumberFormatException) {
                            println("Invalid Input: Must be valid number")
                            valid = false
                        }
                    }while (!valid)
                    iterations += iterationAmount
                    println()
                    iterateBoard(board, iterationAmount - 1)
                    board.printBoard()
                }
                else -> {
                    print("Unrecognized command, try again!\n")
                }
            }
        } while (inputString.compareTo("quit") != 0 && inputString.compareTo("q") != 0)
    }else{
        //multiplayer game
        println("NOT IMPLEMENTED")
        return
    }
}

fun iterateBoard(board: BoardInterface?, n: Int) {
    for(i in 1..n) {
        board?.nextGeneration()
    }
}

fun loadBoard(): BoardInterface?{
    var file: File

    var inputString: String

    //open the file containing the board
    do {
        print("Enter board name: ")
        inputString = readLine()!!
        file = File("boards/$inputString")
    } while(!file.exists())

    //begin reading file
    val reader: BufferedReader = file.bufferedReader()

    //determine if board is infinite or finite
    var input = reader.readLine().split("=")

    if (input[1].trim().toBoolean()){ //infinite board
        reader.readLine() //skip over iterations

        //determine size
        input = reader.readLine().split("=")
        val width = input[1].trim().toInt()
        input = reader.readLine().split("=")
        val height = input[1].trim().toInt()

        //read and create board
        var infBoardLoad = InfiniteBoard()
        infBoardLoad.xMax = width
        infBoardLoad.yMax = height
        for(y in 0 until height) {
            input = reader.readLine().split(" ")
            for(x in 0 until width) {
                infBoardLoad.birthCell(x, y, input[x].trim().toInt())
            }
        }

        return infBoardLoad

    }else{  //finite board
        //determine if board is toroidal or dead edged
        reader.readLine() //skip over iterations
        input = reader.readLine().split("=")

        //determine type of finite board
        val toroidalMode = input[1].trim().toBoolean()

        //determine size of board
        input = reader.readLine().split('=')
        val width = input[1].trim().toInt()
        input = reader.readLine().split("=")
        val height = input[1].trim().toInt()

        //read in board
        val initialBoard = Array(height){ IntArray(width)}
        for (i in 0 until height) {
            input = reader.readLine().split(" ")
            for (j in 0 until width) {
                initialBoard[i][j] = input[j].toInt()
            }
        }

        //create board
        return Board(width, height, toroidalMode, initialBoard)

    }
}

fun intervalIterate(iterationsPerSecond: Double, board: BoardInterface?) {
    val period: Long = (1000/iterationsPerSecond).toLong()
    val timer = Timer()
    println("printing board....")
    board?.printBoard()
    timer.schedule(IterTask(board), 0, period)
    readLine()!!
    timer.cancel()
}

