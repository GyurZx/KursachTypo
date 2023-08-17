package com.example.kursovayarabotamoiseevrpis11.functional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessPriority {
    public static void main(String[] args) throws IOException {
        String processName = "tasklist.exe";
        Process process = Runtime.getRuntime().exec(processName);
        System.out.println(process.pid() + " " + process.info());

//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            System.out.println(line);
//        }

        ProcessHandle.allProcesses().forEach(ph -> {
            try {
                ProcessHandle.Info info = ph.info();
                if (info.command().isPresent() && info.command().get().contains(processName)) {
                    int pid = (int) ph.pid();
                    int priority = getProcessPriority(pid);
                    System.out.println("Process ID: " + pid);
                    System.out.println("Process Priority: " + priority);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static int getProcessPriority(int pid) throws IOException {
        String command = "tasklist.exe /FI \"PID eq " + pid + "\" /FO CSV /NH";
        Process process = Runtime.getRuntime().exec(command);

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        if (line != null) {
            String[] tokens = line.split(",");
            if (tokens.length >= 6) {
                String priorityStr = tokens[5].replaceAll("\"", "").trim();
                return Integer.parseInt(priorityStr);
            }
        }

        return 0;
    }
}
