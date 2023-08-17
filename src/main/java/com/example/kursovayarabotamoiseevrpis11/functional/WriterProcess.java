package com.example.kursovayarabotamoiseevrpis11.functional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class WriterProcess {
    public static void main(String[] args) throws IOException {
        // Create a named pipe
        File pipe = new File("mypipe");
        //pipe.delete();
        Runtime.getRuntime().exec("mkfifo " + pipe.getAbsolutePath());

        // Write something to the named pipe
        String message = "Hello, world!";
        List<String> messages = new ArrayList<>();
        messages.add(message);
        // Display the messages in a ListView table
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(messages);
        listView.setItems(items);
        Stage stage = new Stage();
        Scene scene = new Scene(listView);
        stage.setScene(scene);
        stage.show();

        OutputStream out = new FileOutputStream(pipe);
        out.write(message.getBytes());
        out.close();
    }
}
