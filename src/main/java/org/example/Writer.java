package org.example;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Writer {

    static String FILE = "./file.log";
    public static void logToFile(String msg) {
        try(FileOutputStream fos = new FileOutputStream(FILE, true)) {
            fos.write(msg.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
