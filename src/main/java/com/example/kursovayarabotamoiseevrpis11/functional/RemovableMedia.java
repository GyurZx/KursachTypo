package com.example.kursovayarabotamoiseevrpis11.functional;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class RemovableMedia {
//    public static void main(String[] args) {
//        // Получаем список корневых директорий
//        File[] roots = File.listRoots();
//        // Проходим по каждой корневой директории
//        for (File root : roots) {
//            // Проверяем, является ли директория съемным носителем
//            if (root.canWrite() && root.getTotalSpace() > 0) {
//                // Получаем имя диска
//                String driveName = root.getAbsolutePath().substring(0, 1);
//                // Получаем список файлов в директории
//                File[] files = root.listFiles();
//                // Проходим по каждому файлу
//                for (File file : files) {
//                    // Проверяем, является ли файл диском
//                    if (file.isDirectory() && file.canRead() && !file.isHidden()) {
//                        if(!driveName.equals("C")) {
//                            System.out.println(driveName + ": " + file.getName());
//                        }
//                    }
//                }
//            }
//
////            // копирование
////            File sourceFile = new File("C:\\example.txt");
////            File destFile = new File(root.getAbsolutePath() + "\\example.txt");
////            try (FileInputStream fis = new FileInputStream(sourceFile);
////                 FileOutputStream fos = new FileOutputStream(destFile)) {
////                byte[] buffer = new byte[1024];
////                int length;
////                while ((length = fis.read(buffer)) > 0) {
////                    fos.write(buffer, 0, length);
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
//        }
//    }
//}

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RemovableMedia extends Application {

    private static String DRIVE_NAME;

    public RemovableMedia(String disk) {
        DRIVE_NAME = disk + ":";
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create a TableView to display the files in the drive
        TableView<FileModel> tableView = new TableView<>();
        ObservableList<FileModel> items = FXCollections.observableArrayList();
        tableView.setItems(items);

        // Create a context menu for the table
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            FileModel selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                items.remove(selectedItem);
            }
        });
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setOnAction(event -> {
            File[] files = new File(DRIVE_NAME).listFiles();
            List<FileModel> selectedItems = tableView.getSelectionModel().getSelectedItems();
            if (!selectedItems.isEmpty()) {
                FileModel file = tableView.getSelectionModel().getSelectedItem();
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putFiles(getFiles(selectedItems));
                clipboard.setContents(new Transferable() {
                    @Override
                    public DataFlavor[] getTransferDataFlavors() {
                        return new DataFlavor[] {DataFlavor.javaFileListFlavor};
                    }
                    @Override
                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return flavor.equals(DataFlavor.javaFileListFlavor);
                    }
                    @Override
                    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                        if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                            return Collections.singletonList(file);
                        } else {
                            throw new UnsupportedFlavorException(flavor);
                        }
                    }
                }, null);
            }
        });

//        itemFolder.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
//        itemFile.setAccelerator(KeyCombination.keyCombination("Ctrl+1"));
//        cut.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        copyItem.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
//        paste.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        deleteItem.setAccelerator(KeyCombination.keyCombination("Delete"));
//        rename.setAccelerator(KeyCombination.keyCombination("F2"));
//
        contextMenu.getItems().addAll(copyItem, deleteItem);
        tableView.setContextMenu(contextMenu);

        TableColumn<FileModel, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<FileModel, Long> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        TableColumn<FileModel, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableView.getColumns().addAll(nameColumn, sizeColumn, dateColumn);

        File[] files = new File(DRIVE_NAME).listFiles();
        if (files != null) {
            for (File file : files) {
                    FileModel model = new FileModel(file.getName(), file.length(), new Date(file.lastModified()));
                    items.add(model);
            }
        }
        StackPane root = new StackPane();
        root.getChildren().add(tableView);
        primaryStage.setScene(new Scene(root, 465, 440));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class FileModel {
        private final SimpleStringProperty name;
        private final SimpleLongProperty size;
        private final SimpleObjectProperty<Date> date;
        public FileModel(String name, long size, Date date) {
            this.name = new SimpleStringProperty(name);
            this.size = new SimpleLongProperty(size);
            this.date = new SimpleObjectProperty<>(date);
        }
        public String getName() {
            return name.get();
        }
        public long getSize() {
            return size.get();
        }
        public Date getDate() {
            return date.get();
        }

    }

    private static List<File> getFiles(List<FileModel> items) {
        return items.stream()
                .map(item -> new File(DRIVE_NAME + "\\" + item.getName()))
                .toList();
    }
}