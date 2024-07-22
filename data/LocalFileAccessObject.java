package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LocalFileAccessObject {

    /**
     * Uses a java.io.File object to read the contents of that file into a string
     */
    public String fileToString(File file) {
        String result = "";
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                result += reader.nextLine();
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return result;
    }
    
}
