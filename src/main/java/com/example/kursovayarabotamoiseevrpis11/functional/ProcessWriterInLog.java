package com.example.kursovayarabotamoiseevrpis11.functional;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProcessWriterInLog {
    public static void log(String command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        Process p = pb.start();
        String processName = p.info().command().get();
        LocalDateTime startTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedStartTime = startTime.format(formatter);
        FileWriter writer = new FileWriter("C:\\JP\\KursovayaRabotaMoiseevRPIS11\\src\\main\\java\\com\\example\\kursovayarabotamoiseevrpis11\\functional\\process_log.txt", true);
        writer.write(processName + " started at " + startTime + "\n");
        writer.close();
    }
}