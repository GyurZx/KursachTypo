package com.example.kursovayarabotamoiseevrpis11.functional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class ProcessLogger {

    private static File selectedFile;

    public ProcessLogger() {}

    public ProcessLogger(File file) {
        this.selectedFile = file;
    }
    public static void AllLog() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile, true));
            ProcessHandle.allProcesses().forEach(process -> {
                LocalDateTime now = LocalDateTime.now();
                String processName = process.info().command().orElse("");
                String log = processName + " started at " + now;
                try {
                    writer.write(log);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
