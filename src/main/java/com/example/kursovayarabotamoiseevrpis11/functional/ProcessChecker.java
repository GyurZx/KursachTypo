package com.example.kursovayarabotamoiseevrpis11.functional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProcessChecker extends Application {

    private File selectedFile;
    private TextField cmd;

    public ProcessChecker() {}
    public ProcessChecker(File file) {
        this.selectedFile = file;
    }

    @Override
    public void start(Stage primaryStage) {

        // создаем объект типа ProcessBuilder с командой "ps -e"
        ProcessBuilder pb = new ProcessBuilder("tasklist"); //tasklist "ps axo pid,pri,comm --sort=-pri | grep -v "grep\|/""

        try {
            // запускаем процесс
            Process p = pb.start();
            System.out.println(p.pid() + " " + p.info());

            // получаем вывод процесса
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // создаем список для хранения процессов
            ArrayList<String> processes = new ArrayList<String>();

            // читаем вывод процесса построчно
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String tokens[] = line.split(" "); // cd Desktop tokens[0} = cd, tokens[1] = Desktop
                String processName = tokens[0];
                // исключаем процессы, связанные с нашей программой
                if (!line.contains("java.exe")) {
                    //String tokens[] = line.split(" "); // cd Desktop tokens[0} = cd, tokens[1] = Desktop
                    processes.add(line); // добавляем процесс в список
                    count += 1;
//                    ProcessHandle.allProcesses().forEach(ph -> {
//                        try {
//                            ProcessHandle.Info info = ph.info();
//                            if (info.command().isPresent() && info.command().get().contains(processName)) {
//                                int pid = (int) ph.pid();
//                                int priority = getProcessPriority(pid);
//                                System.out.println("Process ID: " + pid);
//                                System.out.println("Process Priority: " + priority);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    });
                }
            }
            System.out.println(count);
            FileWriter writer = new FileWriter("C:\\JP\\KursovayaRabotaMoiseevRPIS11\\src\\main\\java\\com\\example\\kursovayarabotamoiseevrpis11\\functional\\process_log.txt", true);
            LocalDateTime startTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedStartTime = startTime.format(formatter);
            writer.write("Count System process: " + count + " in time: " + startTime + "\n");
            writer.close();

            // закрываем буферизированный поток
            br.close();

            // создаем список элементов для отображения в ListView
            ObservableList<String> items = FXCollections.observableArrayList(processes);

            // создаем ListView и добавляем элементы
            ListView<String> listView = new ListView<String>(items);

            // создаем контейнер VBox для размещения ListView
            VBox root = new VBox();
            root.setPadding(new Insets(10));
            root.setSpacing(10);
            root.getChildren().add(listView);

            // создаем сцену и отображаем ее
            Scene scene = new Scene(root, 500, 450);
            primaryStage.setTitle("List of Processes");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
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