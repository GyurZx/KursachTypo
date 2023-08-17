package com.example.kursovayarabotamoiseevrpis11.functional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class FlashDriveExplorer extends Application {

    private ListView<File> listView;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setTitle("Поиск файлов");

        listView = new ListView<File>();
        listView.setCellFactory(param -> new ListCell<File>() {
            @Override
            protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }

                if (!empty) {
                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem deleteMenuItem = new MenuItem("delete");
                    deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            item.delete();
                        }
                    });
                    MenuItem cutMenuItem = new MenuItem("cut");
                    cutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent content = new ClipboardContent();
                            content.putFiles(List.of(item));
                            clipboard.setContent(content);
                            item.delete();
                        }
                    });
                    MenuItem copyMenuItem = new MenuItem("copy");
                    copyMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent content = new ClipboardContent();
                            content.putFiles(List.of(item));
                            clipboard.setContent(content);
                        }
                    });
                    MenuItem pasteMenuItem = new MenuItem("paste");
                    pasteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            List<File> files = clipboard.getFiles();
                            if (files != null && files.size() > 0) {
                                File file = files.get(0);
                                if (file.isFile()) {
                                    file.renameTo(new File(item.getParentFile(), file.getName()));
                                }
                            }
                        }
                    });
                    MenuItem createFileMenuItem = new MenuItem("create");
                    createFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            // Prompt the user for a file name
                            TextInputDialog dialog = new TextInputDialog();
                            dialog.setTitle("Создать файл");
                            dialog.setHeaderText(null);
                            dialog.setContentText("Введите имя файла:");

                            Optional<String> result = dialog.showAndWait();
                            if (result.isPresent()) {
                                String fileName = result.get();
                                File file = new File(item, fileName);
                                System.out.println(file);
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    MenuItem renameMenuItem = new MenuItem("rename");
                    renameMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            File selectedFile = listView.getSelectionModel().getSelectedItem();
                            TextInputDialog dialog = new TextInputDialog(selectedFile.getName());
                            dialog.setTitle("Переименовать");
                            dialog.setHeaderText(null);
                            dialog.setContentText("Введите новое имя файла:");

                            Optional<String> result = dialog.showAndWait();
                            if (result.isPresent()) {
                                String newFileName = result.get();
                                File newFile = new File(selectedFile.getParentFile(), newFileName);
                                selectedFile.renameTo(newFile);
                            }
                        }
                    });
                    createFileMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+1"));
                    cutMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
                    copyMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
                    pasteMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
                    deleteMenuItem.setAccelerator(KeyCombination.keyCombination("Delete"));
                    renameMenuItem.setAccelerator(KeyCombination.keyCombination("F2"));
                    contextMenu.getItems().addAll(createFileMenuItem, pasteMenuItem, copyMenuItem, cutMenuItem, renameMenuItem, deleteMenuItem);
                    setContextMenu(contextMenu);
                } else {
                    setContextMenu(null);
                }
            }
        });
        // Prompt the user to select a directory
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        // Use the selected directory to get a list of files
        ObservableList<File> items = FXCollections.observableArrayList();
        File[] files = selectedDirectory.listFiles();
        for (File file : files) {
            items.add(file);
        }

        // Set the items in the ListView
        listView.setItems(items);

        root.setCenter(listView);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
