package com.example.kursovayarabotamoiseevrpis11.functional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReaderProcess {
    public static void main(String[] args) throws IOException {
        // Open the named pipe for reading
        File pipe = new File("mypipe");
        InputStream in = new FileInputStream(pipe);

        // Read from the named pipe and display it in the console
        byte[] buffer = new byte[1024];
        int bytesRead = in.read(buffer);
        System.out.println(new String(buffer, 0, bytesRead));

        // Close the named pipe
        in.close();
    }
}
