package cs1302.game;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.io.File;
import java.util.Scanner;

/**
 * This class represents a MinesweeperGame object. Each {@code MinesweeperGame} has
 * varies instance variables and methods to keep track of a minesweeper game. The only
 * method that can be called by a {@code MinesweeperGame} object is the {@link #play()} method.
 *
 * <p>
 * A {@code MinesweeperGame} object cannot have a grid size less then a 5 x 5
 * dimension. Seed file must also be formatted correctly; including the rows, columns, number of
 * mines, and each mine's location on the grid. The amount of mines cannot exceed
 * the amount of spaces on the grid nor can any of the mines be placed outside of the grid.
 * The constructor of this class will ensure these conditions via exceptions.
 */
public class MinesweeperGame {
    private int rows;
    private int cols;
    private int rounds;
    private int numberOfMines;
    private int xLoc;
    private int yLoc;
    private double score;
    private boolean gameOver;
    private Scanner keyboard;
    private boolean cheatCode;
    private String[] command;
    private String[][] mineField;
    private boolean[][] winProgress;
    private boolean[][] mineLocation;
    private int[][] mineData;

    /**
     * Constructs a {@code MinesweeperGame} object with a given {@code seed} file.
     * Seed file must be formatted correctly; including the rows, columns, number of
     * mines, and each mine's location on the grid. The amount of mines cannot exceed
     * the amount of spaces on the grid nor can any of the mines be placed outside of the grid.
     * The {@link #checkSeed()} method will throw exceptions if conditions
     * fail to be met.
     * @param seed the path to seed file.
     * @throws FileNotFoundException when file is not accessable or cannot be found.
     * @throws NoSuchElementException when missing information in the {@code seed}.
     * @throws NumberFormatException when {@code seed} don't have the right data type
     * or is out of bounds.
     */
    public MinesweeperGame(String seed) {
        try {
            this.rounds = 0;
            this.score = 0.0;
            this.gameOver = false;
            this.cheatCode = false;
            File seedFile = new File(seed);
            Scanner seedScanner = new Scanner(seedFile);
            this.keyboard = new Scanner(System.in);
            this.rows = Integer.parseInt(seedScanner.next());
            this.cols = Integer.parseInt(seedScanner.next());
            numberOfMines = Integer.parseInt(seedScanner.next());
            mineData = new int[numberOfMines][2];
            int x;
            int y;
            for (int i = 0; i < mineData.length; i++) {
                x = Integer.parseInt(seedScanner.next());
                y = Integer.parseInt(seedScanner.next());
                if (isInBounds(x,y)) {
                    mineData[i][0] = x;
                    mineData[i][1] = y;
                } else {
                    throw new NumberFormatException();
                }
            } //for
            checkSeed();
            assignArray();
        } catch (FileNotFoundException fnfe) {
            System.err.println("Seedfile Not Found Error: Cannot create game with " +
                               seed + ", because it cannot be found\n\t\tor cannot be" +
                               " read due to permission.\n");
            System.exit(1);
        } catch (NumberFormatException | NoSuchElementException ex) {
            System.err.println("Seedfile Format Error: Cannot create game with " +
                               seed + ", because it is not formatted correctly.\n");
            System.exit(1);
        } //try
    } //Constructor

    /**
     * Check to see if the {@code seed} file met all the conditions. If not, the
     * method will either throw an exception or give a Seedfile Value error depending
     * on which condition wasn't met.
     * @throws ArrayIndexOutOfBoundsException when values are not within bounds of grid.
     */
    private void checkSeed() {
        if (this.rows < 5 || this.cols < 5) {
            System.err.println("\nSeedfile Value Error: Cannot create a mine field " +
                               "with that many rows and/or columns!");
            System.exit(3);
        } //if
        if (numberOfMines > cols * rows) {
            throw new ArrayIndexOutOfBoundsException();
        } //if
        for (int i = 0; i < mineData.length; i++) {
            if (!isInBounds(mineData[i][0], mineData[i][1])) {
                throw new ArrayIndexOutOfBoundsException();
            } //if
        } //for
    } //method

    /**
     * Initialized the {@code winProgress} and {@code mineLocation} arrays and
     * set every index value to false. Then retreive the locations of the mines
     * from the seed file and set the corresponding location in the {@code mineLocation} to true.
     */
    private void assignArray() {
        winProgress = new boolean[rows][cols];
        mineLocation = new boolean[rows][cols];
        for (int row = 0; row < mineLocation.length; row++) {
            for (int col = 0; col < mineLocation[row].length; col++) {
                mineLocation[row][col] = false;
                winProgress[row][col] = false;
            } //for
        } //for
        for (int i = 0; i < mineData.length; i++) {
            mineLocation[mineData[i][0]][mineData[i][1]] = true;
        } //for
    } //method

    /**
     * Print the welcome banner for the minesweeper game.
     */
    private void printWelcome() {
        System.out.println("        _\n" +
                           "  /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __\n" +
                           " /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__" +
                           "|\n/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |\n" +
                           "\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|\n" +
                           "                 A L P H A   E D I T I O N |_| v2020.sp");
    } //method

    /**
     * Assign the value to minesweeper's game board. Assign strings to the
     * mineField array that represent the current state of the game. The game
     * board will be padded depending on the numbers of rows and columns.
     * @param num the case that should be called depending on the length of rows and columns.
     * @param spaces the amount of whitespaces that the indexs should be padded.
     */
    private void printMineFieldHelper(int num, int spaces) {
        switch (num) {
        case 1:
            for (int i = 0; i < this.rows; i++) {
                String number = "%" + spaces + "d";
                mineField[i][0] = "%s" + number;
            } //for
            int padding = spaces - 1;
            mineField[rows][0] = " %" + ((spaces * 2) - padding) + "s";
            break;
        case 2:
            for (int i = 1; i <= this.cols * 2; i += 2) {
                String number = "%" + spaces + "d";
                mineField[rows][i] = " %s" + number;
                mineField[rows][i + 1] = " ";
            } //for
            for (int row = 0; row < this.rows; row++) {
                for (int col = 1; col <= this.cols * 2; col += 2) {
                    mineField[row][col] = " |%" + spaces + "s";
                    mineField[row][col + 1] = " ";
                } //for
            } //for
            for (int i = 0; i < this.rows; i++) {
                mineField[i][(cols * 2) + 1] = " |";
            } //for
            break;
        case 3:
            if (cheatCode) {
                for (int i = 0; i < mineData.length; i++) {
                    mineField[mineData[i][0]][(mineData[i][1] * 2) + 1] = " |%" + spaces + "s";
                    mineField[mineData[i][0]][(mineData[i][1] * 2) + 3] = " |%" + spaces + "s";
                } //for
                cheatCode = false;
            } //if
            break;
        case 4:
            if (spaces > 1) {
                for (int i = 0; i < mineData.length; i++) {
                    int x = mineData[i][0];
                    int y = mineData[i][1];
                    String r = mineField[x][(y * 2) + 1];
                    String l = mineField[x][(y * 2) + 3];
                    mineField[x][(y * 2) + 1] = r.substring(0,2) + "<%" + (spaces - 1) + "s";
                    mineField[x][(y * 2) + 3] = ">" + l.substring(1);
                } //for
                cheatCode = true;
            } else {
                for (int i = 0; i < mineData.length; i++) {
                    String r = mineField[mineData[i][0]][(mineData[i][1] * 2) + 1];
                    String l = mineField[mineData[i][0]][(mineData[i][1] * 2) + 3];
                    mineField[mineData[i][0]][(mineData[i][1] * 2) + 1] = r.substring(0,2) + "<";
                    mineField[mineData[i][0]][(mineData[i][1] * 2) + 3] = ">" + l.substring(1);
                } //for
                cheatCode = true;
            } //if else
            break;
        } //switch
    } //method

    /**
     * Calculate the amount of spaces that need to be padded
     * for the {@link printMineFieldHelper} method to use. This
     * return a integer based on the number of digits in the rows and cols.
     * @param digit the number use to calulate the spacing.
     * @return the number of whitespace to pad.
     */
    private int spaceNum(int digit) {
        double widthDouble = Math.ceil(Math.log10(digit));
        int widthInt = 1;
        if (digit != 1) {
            widthInt = (int) widthDouble;
        } //if
        return widthInt;
    } //method

    /**
     * Print the minesweeper's game board to standard output. Also check
     * if cheat code is active and switch it off.
     */
    private void printMineField() {
        System.out.println("\n Rounds Completed: " + rounds + "\n");
        if (rounds == 0) {
            mineField = new String[rows + 1][(cols * 2) + 2];
            printMineFieldHelper(1,spaceNum(rows));
            printMineFieldHelper(2,spaceNum(cols));
        } //if
        for (int i = 0; i < mineField.length - 1; i++) {
            for (int j = 0; j < mineField[i].length; j++) {
                System.out.printf(mineField[i][j]," ",i);
            } //for
            System.out.println();
        } //for
        System.out.printf(mineField[rows][0]," ");
        int xNum = 0;
        int j = 2;
        for (int i = 1; i < mineField[rows].length - 1; i += 2) {
            System.out.printf(mineField[rows][i]," ",(int)xNum);
            System.out.printf(mineField[rows][j]);
            xNum++;
            j += 2;
        } //for
        System.out.println();
        printMineFieldHelper(3,spaceNum(cols));
    } //method

    /**
     * Determine if the user has inputed numbers and return true if
     * the input is a number and false if it is not. Assign the inputs
     * to {@code xLoc} and {@code yLoc} respectively in the order of inputs.
     * @return true if the user input is a number and false if the input is not.
     * @throws NumberFormatException when input command are not numbers.
     */
    private boolean isNumber() {
        try {
            if (command[1] != null && command[2] != null) {
                Double.parseDouble(command[1]);
                Double.parseDouble(command[2]);
                int x = Integer.parseInt(command[1]);
                int y = Integer.parseInt(command[2]);
                if (isInBounds(x,y)) {
                    this.xLoc = x;
                    this.yLoc = y;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            } //if else
        } catch (NumberFormatException nfe) {
            return false;
        } //try
    } //method

    /**
     * Checks if a square is within the game board grid.
     * @param row the x value of the square.
     * @param col the y value of the square.
     * @return true if the square is in bounds of the game board and false if
     * the square is out of bounds.
     */
    private boolean isInBounds(int row, int col) {
        if (row < this.rows && row >= 0 && col < this.cols && col >= 0) {
            return true;
        } else {
            return false;
        } //if else
    } //method

    /**
     * Checks if the user won or lost the game then prompt the user for a input
     * and store the command to the {@code command} array.
     */
    private void promptUser() {
        System.out.print("\nminesweeper-alpha: ");
        command = keyboard.nextLine().split(" ");
        String[] temp = new String[3];
        int index = 0;
        for (int i = 0; i < command.length; i++) {
            if (index == 3) {
                i = command.length;
            } //if
            if (!command[i].equals("")) {
                temp[index] = command[i];
                index++;
            } //if
        } //for
        this.command = temp;
    } //method

    /**
     * Find the number of mines touching a square.
     * @param row the x value of the square.
     * @param col the y value of the square.
     * @return the number of mines touching the square.
     */
    private int getNumAdjMines(int row, int col) {
        int numAdjMines = 0;
        for (int i = 0; i < mineData.length; i++) {
            if (mineData[i][0] == row + 1 && mineData[i][1] == col) {
                numAdjMines++;
            } else if (mineData[i][0] == row && mineData[i][1] == col + 1) {
                numAdjMines++;
            } else if (mineData[i][0] == row - 1 && mineData[i][1] == col) {
                numAdjMines++;
            } else if (mineData[i][0] == row && mineData[i][1] == col - 1) {
                numAdjMines++;
            } else if (mineData[i][0] == row + 1 && mineData[i][1] == col - 1) {
                numAdjMines++;
            } else if (mineData[i][0] == row + 1 && mineData[i][1] == col + 1) {
                numAdjMines++;
            } else if (mineData[i][0] == row - 1 && mineData[i][1] == col + 1) {
                numAdjMines++;
            } else if (mineData[i][0] == row - 1 && mineData[i][1] == col - 1) {
                numAdjMines++;
            } //else if
        } //for
        return numAdjMines;
    } //method

    /**
     * Determine if the player have won the game or not by checking the {@code winProgress}
     * array. The player win the game if all the values in the {@code winProgress} is true.
     * @return true if the user won the game and false if the player did not.
     */
    private boolean isWon() {
        boolean winConditionMet = false;
        int counter = 0;
        for (int row = 0; row < winProgress.length; row++) {
            for (int col = 0; col < winProgress[row].length; col++) {
                if (winProgress[row][col] == true) {
                    counter++;
                } //if
            } //for
        } //for
        if (counter == rows * cols) {
            winConditionMet = true;
        } //if
        return winConditionMet;
    } //method

    /**
     * Determine if the player have lost the game or not by checking if the player have reveal any
     * mines. If the player has reveal a mine location, the method will return true.
     * @return true if the player reveal a mine otherwise return false.
     */
    private boolean isLost() {
        boolean loseConditionMet = false;
        if (mineLocation[xLoc][yLoc] == true) {
            loseConditionMet = true;
        } //if
        return loseConditionMet;
    } //method

    /**
     * Print the win screen of the minesweeper game. Also display the score of the player.
     */
    private void printWin() {
        System.out.print("\n ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"\n" +
                         " ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░\n" +
                         " ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"\n" +
                         " ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░\n" +
                         " ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"\n" +
                         " ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░\n" +
                         " ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"\n" +
                         " ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░\n" +
                         " ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░\n" +
                         " ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░\n" +
                         " ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░\n" +
                         " ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌\n" +
                         " ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░\n" +
                         " ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░\n" +
                         " ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░\n" +
                         " ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░\n" +
                         " ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!\n" +
                         " ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!\n");
        System.out.printf(" ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: %.2f\n\n", score);
    } //method

    /**
     * Print the lose screen of the minesweeper game.
     */
    private void printLoss() {
        System.out.println("\n Oh no... You revealed a mine!");
        System.out.println("  __ _  __ _ _ __ ___   ___    _____   _____ _ __\n" +
                           " / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|\n" +
                           "| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |\n" +
                           " \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|\n" +
                           " |___/\n");
    } //method

    /**
     * Holds all the command conditions for the {@link #play()} method. Checks if the user
     * have inputed valid commands and return true if the case's conditions are met.
     * @param num the case number to check certain conditions.
     * @return true if all conditons if a case are met, otherwise return false.
     */
    private boolean playCommandHelper (int num) {
        switch (num) {
        case 0:
            if (command[0].equals("r") || command[0].equals("reveal")) {
                if (isNumber()) {
                    return true;
                } else {
                    return false;
                } //if else
            } //if
            break;
        case 1:
            if (command[0].equals("m") || command[0].equals("mark")) {
                if (isNumber()) {
                    return true;
                } else {
                    return false;
                } //if else
            } //if
            break;
        case 2:
            if (command[0].equals("g") || command[0].equals("guess")) {
                if (isNumber()) {
                    return true;
                } else {
                    return false;
                } //if else
            } //if
            break;
        case 3:
            if (command[0].equals("h") || command[0].equals("help")) {
                if (command[1] == null && command[2] == null) {
                    return true;
                } else {
                    return false;
                } //if else
            } //if
            break;
        case 4:
            if (command[0].equals("q") || command[0].equals("quit")) {
                if (command[1] == null && command[2] == null) {
                    return true;
                } else {
                    return false;
                } //if else
            } //if
            break;
        } //switch
        return false;
    } //method

    /**
     * The main game loop that plays the minesweeper game. The method will display the board and
     * ask the user for a command via the {@link #printMineField()} and {@link #promptUser()}
     * methods. The user can either reveal a square, mark a square, guess a square, get help, and
     * quit the game. There is also a nofog command that will reveal the mines location for one
     * round. The game will update every actions that the player take to the game board and print
     * it back to standard output. Game will end either when the player won or lose then print
     * the corresponding win or lose screen via the {@link #printWin()} and {@link #printLoss()}
     * methods.
     * @throws ArrayIndexOutOfBoundsException when square that command is used on is not in bounds.
     */
    public void play() {
        printWelcome();
        do {
            try {
                printMineField();
                promptUser();
                if (command[0] == null) {
                    throw new ArrayIndexOutOfBoundsException();
                } else if (playCommandHelper(0)) {
                    mineField[xLoc][(yLoc * 2) + 2] = Integer.toString(getNumAdjMines(xLoc,yLoc));
                    winProgress[xLoc][yLoc] = true;
                    gameOver = isWon() || isLost();
                } else if (playCommandHelper(1)) {
                    mineField[xLoc][(yLoc * 2) + 2] = "F";
                    if (mineLocation[xLoc][yLoc] == true) {
                        winProgress[xLoc][yLoc] = true;
                    } else if (winProgress[xLoc][yLoc] == true) {
                        winProgress[xLoc][yLoc] = false;
                    } //else if
                    gameOver = isWon();
                } else if (playCommandHelper(2)) {
                    mineField[xLoc][(yLoc * 2) + 2] = "?";
                    if (winProgress[xLoc][yLoc] == true) {
                        winProgress[xLoc][yLoc] = false;
                    } //if
                } else if (playCommandHelper(3)) {
                    System.out.println("\nCommands Available...\n" +
                                       " - Reveal: r/reveal row col\n" +
                                       " -   Mark: m/mark   row col\n" +
                                       " -  Guess: g/guess  row col\n" +
                                       " -   Help: h/help\n" +
                                       " -   Quit: q/quit");
                } else if (playCommandHelper(4)) {
                    System.out.println("\nQuitting the game...\nBye!");
                    System.exit(0);
                } else if (command[0].equals("nofog")) {
                    printMineFieldHelper(4,spaceNum(cols));
                } else {
                    System.err.println("\nInput Error: Command not recognized!");
                    rounds--;
                } //else if
                rounds++;
            } catch (ArrayIndexOutOfBoundsException aiobe) {
                System.err.println("\nInput Error: Command not recognized!");
            } //try
        } while (!gameOver); //do while
        this.score = 100.0 * rows * cols / rounds;
        if (isWon()) {
            printWin();
        } else {
            printLoss();
        } //if else
        System.exit(0);
    } //method
} //class
