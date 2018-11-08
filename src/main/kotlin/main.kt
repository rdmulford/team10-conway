import java.io.File
import java.io.BufferedReader

fun main(args : Array<String>) {
    var inputString: String
    var iterations = 0

    println("Team 10: Conway's Game of Life Started")

    //determine type of session
    do {

        print("Load saved board or create new board? (load / new): ")

        inputString = readLine()!!

    } while((inputString.compareTo("load") != 0) and (inputString.compareTo("new") != 0))

    //new board
    if (inputString.compareTo("new") == 0){
        println("NOT IMPLEMENTED")
    }

    //load board
    if (inputString.compareTo("load") == 0){
        val board = loadBoard()

        board?.printBoard()

        do {
            print("Iteration[$iterations]: Enter next(n) to iterate once. Enter iterate(i) to iterate a given number of times. Enter quit(q) to quit: ")
            inputString = readLine()!!
            println()
            if(inputString.compareTo("next") == 0 || inputString.compareTo("n") == 0) {
                board?.nextGeneration()
                board?.printBoard()
                iterations += 1
            } else if(inputString.compareTo("quit") == 0 || inputString.compareTo("q") == 0) {
                print("Quitting, thanks for running our awesome program\n")
            } else if(inputString.compareTo("iterate") == 0 || inputString.compareTo("i") == 0) {
                print("How many iterations?: ")
                var iterationAmount = readLine()!!
                iterations += iterationAmount.toInt()
                println()
                iterateBoard(board, iterationAmount.toInt() - 1)
                board?.printBoard()
            } else {
                print("Unrecognized command, try again!\n")
            }
        }while(inputString.compareTo("quit") != 0 && inputString.compareTo("q") != 0)
    }
}

fun iterateBoard(board: Board?, n: Int) {
    for(i in 1..n) {
        board?.nextGeneration()
    }
}

fun loadBoard(): Board?{
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
    if (input[1].toBoolean()){ //infinite board
        println("NOT IMPLEMENTED")
        return null
    }else{  //finite board
        //determine if board is toroidal or dead edged
        reader.readLine() //skip over iterations
        input = reader.readLine().split("=")
        if (input[1].toBoolean()){ //toroidal board
            println("NOT IMPLEMENTED")
            return null
        }else{ //dead edged board
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
            return Board(width, height, initialBoard)
        }
    }
}