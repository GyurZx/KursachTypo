package com.example.kursovayarabotamoiseevrpis11;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class SystemCommand {
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.load("c", CLibrary.class);

        int system(String cmd);
    }

    public static void main(String[] args) {
        // Выполнение команды в терминале Linux
        CLibrary.INSTANCE.system("ls -l");

        CLibrary.INSTANCE.system("ps -e");

        // Выполнение команды с аргументами
        String[] cmd = {"/bin/sh", "-c", "echo 'Hello, world!'"};
        CLibrary.INSTANCE.system(String.join(" ", cmd));
    }
}
