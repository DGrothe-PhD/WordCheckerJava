package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Readinfile {
    public Readinfile(String filename) {

        File file = new File(filename);

        try (FileInputStream fis = new FileInputStream(file)) {
            System.out.println("Total file size to read (in bytes) : "+ fis.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
