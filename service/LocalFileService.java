package service;

import data.LocalFileAccessObject;
import java.io.File;

/**
 * In this implementation, the LocalFileService does basically nothing. In practice, the service layer would be used
 * to validate and modify user input before passing it on to the persistance layer, often a Database Access Object.
 * Here, as no validation or other work is required, this layered approach would still aid in integration testing,
 * as we could simply drop in a mock LocalFileAccessObject to see what is going on.
 */
public class LocalFileService {
    
    private final LocalFileAccessObject lfao;

    public LocalFileService() {
        this.lfao = new LocalFileAccessObject();
    }
    
    public String fileToString(File file) {
        return lfao.fileToString(file);
    }

}
