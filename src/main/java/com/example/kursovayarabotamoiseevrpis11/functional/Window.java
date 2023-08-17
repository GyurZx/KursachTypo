package com.example.kursovayarabotamoiseevrpis11.functional;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Window extends Application {
    Stage window;
    Button chooseButton, showButton;
    TextField directoryTextField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("File Explorer");

        // Create choose button
        chooseButton = new Button("Choose Directory");
        chooseButton.setOnAction(e -> {
            // Display directory chooser dialog
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(window);
            if (selectedDirectory != null) {
                directoryTextField.setText(selectedDirectory.getAbsolutePath());
            }
        });

        // Create directory text field
        directoryTextField = new TextField();
        directoryTextField.setPromptText("Choose a directory...");

        // Create show button
        showButton = new Button("Show Files");
        showButton.setOnAction(e -> {
            // Get the selected directory
            String directoryPath = directoryTextField.getText();
            File directory = new File(directoryPath);

            // Display files in the selected directory
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                String fileNames = "";
                for (File file : files) {
                        fileNames += file.getName() + "\n";
                }
                if (!fileNames.equals("")) {
                    Label fileLabel = new Label(fileNames);
                    Scene fileScene = new Scene(fileLabel, 400, 400);
                    Stage fileWindow = new Stage();
                    fileWindow.setTitle("Files in " + directoryPath);
                    fileWindow.setScene(fileScene);
                    fileWindow.show();
                } else {
                    Label emptyLabel = new Label("This directory is empty.");
                    Scene emptyScene = new Scene(emptyLabel, 200, 200);
                    Stage emptyWindow = new Stage();
                    emptyWindow.setTitle("Empty Directory");
                    emptyWindow.setScene(emptyScene);
                    emptyWindow.show();
                }
            } else {
                Label errorLabel = new Label("Please choose a valid directory.");
                Scene errorScene = new Scene(errorLabel, 200, 200);
                Stage errorWindow = new Stage();
                errorWindow.setTitle("Invalid Directory");
                errorWindow.setScene(errorScene);
                errorWindow.show();
            }
        });

        // Create a layout
        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(8);
        layout.setHgap(10);

        // Add components to the layout
        GridPane.setConstraints(directoryTextField, 0, 0, 2, 1);
        GridPane.setConstraints(chooseButton, 2, 0);
        GridPane.setConstraints(showButton, 0, 1, 3, 1);
        layout.getChildren().addAll(directoryTextField, chooseButton, showButton);

        // Create a scene
        Scene scene = new Scene(layout, 350, 100);
        window.setScene(scene);
        window.show();
    }
}
