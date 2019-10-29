package io;

import controller.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * The class which contains the static methods for file output
 */
public class GameWriter {


    /**
     * Writes a board and the move history to a file
     *
     * @param filepath   The path to the cgf file in the filesystem where the information should be written to
     * @param controller The controller which contains the board to read the pieces from
     * @throws IOException when file cannot be written in file system
     */
    public static void write(String filepath, Controller controller) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filepath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(controller.generateGameState());
        out.close();
        fileOut.close();
    }
}
