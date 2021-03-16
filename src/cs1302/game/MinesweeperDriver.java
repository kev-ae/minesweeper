package cs1302.game;

import cs1302.game.MinesweeperGame;
import java.io.File;
import java.util.Random;
import java.io.PrintWriter;
import java.io.IOException;

/**
* This class represent a MinesweeperDriver object. The {@code MinesweeperDriver} class holds
* the main method to run a game of minesweeper. The class must be run by the command line
* argument. There are two types of actions that this program can do: --seed and --gen. For
* a seed file, the --seed command must be use and be formatted: --seed PATH_TO_SEED. If user
* wise to generate a random txt file, use the --gen command. This command must be in this
* format: --gen PATH_TO_SEED ROWS COLS MINES. Main will ensure conditions via exceptions.
*/
public class MinesweeperDriver {

    /**
     * This is the main method of the {@code MinesweeperDriver} program. Method will take
     * command line arguments and correctly determine which set of actions to take. All
     * conditions checking for the --seed command will be check by the {@link #MinesweeperGame}
     * class. For the --gen command, rows and columns cannot be less then a 5 x 5 grid. --Gen
     * command assumes that the number of mines will be less than rows x columns. If a
     * coordinate randomly generated already have a mine, it will regenerate a new position.
     * @param args user command argument line.
     * @throws IOException when user fail to meet gen conditions.
     * @throws ArrayIndexOutOfBoundsException when user fail to provide a correct command.
     */
    public static void main(String[] args) {
        try {
            if (args[0].equals("--seed")) {
                MinesweeperGame game = new MinesweeperGame(args[1]);
                game.play();
            } else if (args[0].equals("--gen")) {
                File fileToMake = new File(args[1]);
                int rows = Integer.parseInt(args[2]);
                int cols = Integer.parseInt(args[3]);
                int numOfMines = Integer.parseInt(args[4]);
                if (rows < 5 && cols < 5) {
                    System.err.println("Format Error: Cannot have a grid that is less then 5 x 5");
                    System.exit(2);
                } //if

                Random xInt = new Random();
                Random yInt = new Random();
                int[][] mines = new int[numOfMines][2];
                for (int i = 0; i < mines.length; i++) {
                    mines[i][0] = xInt.nextInt(rows);
                    mines[i][1] = yInt.nextInt(cols);
                    for (int j = 0; j <= i - 1; j++) {
                        if (mines[i][0] == mines[j][0] && mines[i][1] == mines[j][1]) {
                            j = i;
                            i--;
                        } //if
                    } //for
                } //for

                PrintWriter writer = new PrintWriter(fileToMake);
                writer.print(rows + " " + cols + "\n" + numOfMines + "\n");
                for (int i = 0; i < numOfMines; i++) {
                    writer.print(mines[i][0] + " " + mines[i][1] + "\n");
                } //for
                writer.close();
            } else {
                throw new IOException();
            } //if else
        } catch (IOException | ArrayIndexOutOfBoundsException ex) {
            System.err.println("Unable to interpret supplied command-line arguments.");
            System.exit(1);
        } //try
    } //main
} //class
