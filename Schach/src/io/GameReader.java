package io;

import controller.Controller;
import engine.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * The class which contains the static methods for file input and parsing
 */
public class GameReader {

    /**
     * reads a file, parses it and saves values to a Controller
     *
     * @param filepath   The path of the file in the filesystem
     * @param controller The controller instance where the parsed information should be saved
     * @throws IOException    when file system errors occur
     * @throws ClassNotFoundException when the file referenced by filepath contains unparsable information
     */
    public static void read(String filepath, Controller controller) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(filepath);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        GameState e = (GameState) in.readObject();
        in.close();
        fileIn.close();
        controller.loadGameState(e);
    }
}
