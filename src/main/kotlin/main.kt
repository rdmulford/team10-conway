import java.io.File
import java.io.BufferedReader
import java.util.*

fun main(args : Array<String>) {
    var inputString: String
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

    if (!multiplayer) {
        do {
            print("Load saved board or create new board? (load / new): ")
            inputString = readLine()!!
        } while ((inputString.compareTo("load") != 0) and (inputString.compareTo("new") != 0))

        //new board
        if (inputString.compareTo("new") == 0){
            println("NOT IMPLEMENTED")
            return
        }

        //load board
        if (inputString.compareTo("load") == 0) {
            board = loadBoard()
        }

        //check for successful board creation
        if (board != null) {
            println("Initial Board:")
            board.printBoard()
        }else{
            println("ERROR: board generation or load has failed")
            return
        }
        playSinglePlayer(board)
    }else{
        print("How many players (2-4 defaults to 2): ")
        inputString = readLine()!!
        val players = inputString.toInt()
        print("How many turns: ")
        inputString = readLine()!!
        val turns = inputString.toInt()
        board = generateMultiplayerBoard(players)
        playMultiplayer(board!!, players, turns)
    }
}

fun generateMultiplayerBoard(players: Int): Board? {
    val board :Board?
    if (players == 2) {
        board = loadBoardFile("twoPlayer.brd")
    } else if (players == 3) {
        board = loadBoardFile("threePlayer.brd")
    } else if (players == 4) {
        board = loadBoardFile("fourPlayer.brd")
    } else {
        // invalid player count, just assume two players and move on
        board = loadBoardFile("twoPlayer.brd")
    }
    return board
}

// play a multiplayer game
fun playMultiplayer(board: BoardInterface, players: Int, turns: Int) {
    var iterations = 0
    var inputString = ""
    var turn  = 0
    var isPlayerAlive = BooleanArray(players+1) // array starts at 1
    for (i in 1 until players+1) {
        isPlayerAlive[i] = true
    }

    do {
        // handle each player
        // player is going to start at 1
        for (player in 1 until players+1) {
            if (isPlayerAlive[player]) {
                board.printBoardLineHelpers()
                print("Iteration[$iterations]\nPlayer[$player] place (p) kill(k) quit (q)")
                inputString = readLine()!!
                when {
                    (inputString.compareTo("place") == 0 || inputString.compareTo("p") == 0) -> {
                        var xPos: Int
                        var yPos: Int
                        do {
                            do {
                                print("Enter cell X pos (0-19): ")
                                xPos = readLine()!!.toInt()
                                print("Enter cell Y pos (0-19): ")
                                yPos = readLine()!!.toInt()
                                if ((xPos < 0 || xPos > 19) && (yPos < 0 || yPos > 19)) {
                                    println("Invalid X Y pos, try again")
                                }
                            } while ((xPos < 0 || xPos > 19) && (yPos < 0 || yPos > 19))

                            var isValid: Boolean
                            isValid = board.birthCell(xPos, yPos, player)
                            if(!isValid) {
                                println("Invalid board position, pick a better one")
                            }
                        } while (!isValid)
                    }
                    (inputString.compareTo("kill") == 0 || inputString.compareTo("k") == 0) -> {
                        var xPos: Int
                        var yPos: Int
                        do {
                            do {
                                print("Enter cell X pos (0-19): ")
                                xPos = readLine()!!.toInt()
                                print("Enter cell Y pos (0-19): ")
                                yPos = readLine()!!.toInt()
                                if ((xPos < 0 || xPos > 19) && (yPos < 0 || yPos > 19)) {
                                    println("Invalid X Y pos, try again")
                                }
                            } while ((xPos < 0 || xPos > 19) && (yPos < 0 || yPos > 19))

                            var isValid: Boolean
                            isValid = board.killCell(xPos, yPos, player)
                            if (!isValid) {
                                println("Invalid board position, pick a better one")
                            }
                        } while (!isValid)
                    }
                    (inputString.compareTo("quit") == 0 || inputString.compareTo("q") == 0) -> {
                        print("Quitting!")
                        return
                    }
                }
                println()
            }
        }
        board.nextGeneration()
        iterations += 1

        for (player in 1 until players+1) {
            if(!board.isAlive(player)) {
                println("Player[$player] is dead!")
                isPlayerAlive[player] = false
            }
        }

        if(!isAnyoneAlive(isPlayerAlive)) {
            println("Everyone is dead")
            board.printBoard()
            break
        }

        var winner = checkWinCondition(isPlayerAlive)
        if(winner != -1) {
            println("The winning player is $winner")
            board.printBoard()
            break
        }

        // check turn limit
        if(turn == turns) {
            println("Turn limit reached! Result board: ")
            board.printBoard()
            break
        }
        turn += 1
    } while (inputString.compareTo("quit") != 0 && inputString.compareTo("q") != 0)

    return
}

// is there a winner
fun checkWinCondition(isPlayerAlive: BooleanArray): Int {
    var playersAlive = 0
    var player = 1
    var winningPlayer = 0
    for (playerStatus in isPlayerAlive) {
        if(playerStatus) {
            playersAlive += 1
            winningPlayer = player
        }
        player += 1
    }
    if(playersAlive == 1) {
        return winningPlayer
    }
    return -1
}

// is anyone alive
fun isAnyoneAlive(isPlayerAlive: BooleanArray): Boolean {
    for (playerStatus in isPlayerAlive) {
        if (playerStatus) {
            return true
        }
    }
    return false
}

// play a singleplayer game
fun playSinglePlayer(board: BoardInterface) {
    var iterations = 0
    var inputString: String
    do {
        print("Iteration[$iterations]\nnext (n), quit (q), iterate (i), save (s), play (p): ")
        inputString = readLine()!!
        println()
        when {
            (inputString.compareTo("play") == 0 || inputString.compareTo("p") == 0) -> {
                //iterations per second
                var valid: Boolean
                var iterationsPerSecond: Double = 0.0
                do {
                    valid = true
                    print("Number of iterations per second: ")
                    inputString = readLine()!!
                    try {
                        iterationsPerSecond = inputString.toDouble()
                        if(iterationsPerSecond < 1){
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
                println("NOT IMPLEMENTED")
                println()
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
}

fun iterateBoard(board: BoardInterface?, n: Int) {
    for(i in 1..n) {
        board?.nextGeneration()
    }
}

fun loadBoardFile(filename: String): Board?{
    var inputString: String
    var file = File("boards/$filename")

    //begin reading file
    val reader: BufferedReader = file.bufferedReader()

    //determine if board is infinite or finite
    var input = reader.readLine().split("=")
    if (input[1].trim().toBoolean()){ //infinite board
        println("NOT IMPLEMENTED")
        return null
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
        println("NOT IMPLEMENTED")
        return null
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

