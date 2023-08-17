package com.example.kursovayarabotamoiseevrpis11.fileManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FileUtils;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {

    private TextField cmd, insert_directory;
    private TextArea cmd_info;
    private String currentFolder; // указывает на папку в которой находится
    private String root; // корень

    public FileManager(String currentFolder) {
        this.currentFolder = currentFolder;
        this.root = currentFolder;
    }

    public void listOfFile(boolean withSize) {
        File currentFolderAsFile = new File(currentFolder);
        // Показывает все файлы в текущей папке
        File files[] = currentFolderAsFile.listFiles();
        // По всему массиву файлов в папке
        for(File file : files) {
            if(file.isDirectory()) {
                if(withSize) {
                    System.out.println(file.getName() + "\\" + FileUtils.sizeOfDirectory(file));
                } else {
                    System.out.println(file.getName() + "\\");
                }
            } else {
                if(withSize) {
                    System.out.println(file.getName() + " " + file.length());
                } else {
                    System.out.println(file.getName());
                }
            }
        }
    }

    public void transitionSystemDisk(String DiskName) {
        this.root = DiskName + ":\\";
        this.currentFolder = this.root;
    }

    public void applicationLaunch(String exePath) throws IOException {
        try {
            Process p = Runtime.getRuntime().exec(exePath);
        } catch (Exception e) {
            System.err.println("Не удалось запустить приложение:");
        }
    }

    public void copyFile(String sourceFileName, String deskFileName) throws IOException {
        File source = new File(currentFolder + "\\" + sourceFileName);
        File dest = new File(currentFolder + "\\" + deskFileName);
        try {
            FileUtils.copyFile(source, dest);
        } catch (IOException e) {
            System.err.println("Ошибка копирования файла:");
        }
    }

    public void delete(String deleteFile) {
        File file = new File(currentFolder + "\\" + deleteFile);
        if(deleteFile.equals("C:\\SuperApp\\System")) {
            System.err.println("Нельзя удалить системную папку");
        } else {
            System.out.println("Процесс..");
            file.delete();
        }
    }

    public void createFile(String fileName) throws IOException {
        File file = new File(currentFolder + "\\" + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла в данной директории:");
        }
    }

    public void fileContent(String fileName) throws IOException {
        File file = new File(currentFolder + "\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.err.println("Произошка ошибка при чтении файла:");
        }
    }

    public void changeDirectory(String folderName) {
        // cd / -> root
        // cd .. -> на уровень выше
        // root\\documents\\sub
        if(folderName.equals("/")) {
            this.currentFolder = this.root; // переход в корень
        } else if(folderName.equals("..")) {
            int startLastFolderPosition = this.currentFolder.lastIndexOf("\\"); // root\\documents\\ <- sub
            this.currentFolder = this.currentFolder.substring(0, startLastFolderPosition); // root\\documents\\
        } else {
            this.currentFolder = this.currentFolder + "\\" + folderName;
        }
    }

    public void help() {
        System.out.println("Команда выхода: " + Commands.EXIT);
        System.out.println("Показ файлов и их размера: " + Commands.LIST_OF_FILE_WITH_SIZE);
        System.out.println("Показ файлов: " + Commands.LIST_OF_FILE);
        System.out.println("Создание файлов: " + Commands.CREATE_FILE);
        System.out.println("Команда удаления файла/папки: " + Commands.DELETE);
        System.out.println("Показать содержимое файлов: " + Commands.FILE_CONTENT);
        System.out.println("Изменение директории: " + Commands.CHANGE_DIRECTORY);
        System.out.println("Создать папку: " + Commands.CREATE_DIRECTORY);
        System.out.println("Копирование файлов: " + Commands.COPY_FILE);
        System.out.println("Переменовать файл: " + Commands.RENAME);
        System.out.println("Показать параметры файла: " + Commands.SHOW_FILE);
        System.out.println("Показать путь к файлу: " + Commands.SHOW_PATH_FILE);
        System.out.println("Переместить файл в папку: " + Commands.MOVE_FILE);
    }

    public void rename(String actualFile, String renameFile) {
        File file = new File(currentFolder + "\\" + actualFile);
        File newFile = new File(currentFolder + "\\" + renameFile);
        if(file.equals("C:\\SuperApp\\System")) {
            System.err.println("Ошибка, нельзя переменовать System");
        } else {
            file.renameTo(newFile);
        }
    }

    public void showFileInfo(String showFile) {
        File file = new File(currentFolder + "\\" + showFile);
        System.out.println(file.getName() + " " + file.length());
    }

    public void showPath() {
        File file = new File(currentFolder);
        System.out.println(file.getPath());
    }

    public void createDirectoryIn(String createDirectory) {
        File directory = new File(currentFolder + "\\" + createDirectory);
        directory.mkdir();
    }

    public void move(String moveFile, String moveInDirectory) throws IOException {
        File fileMove = new File(currentFolder + "\\" + moveFile);
        File fileIn = new File(moveInDirectory + "\\" + moveFile);
        // копируем файл из той директории где он был, в которую необходимо пользователю
        try {
            FileUtils.copyFile(fileMove, fileIn);
        } catch (IOException e) {
            System.err.println("При переносе произошла ошибка:");
        }
        // удаляем файл из директории, оставляя в той где нужно пользователю
        fileMove.delete(); // помоему тут может просто удалиться файл, а при копировании произойти ошибка..
    }

    public void deleteTrash(String moveFile) throws IOException {
        File fileMoveTrash = new File(currentFolder + "\\" + moveFile);
        File fileIn = new File("C:\\SuperApp\\TrashBin\\" + moveFile);
        // копируем файл из той директории где он был, в которую необходимо пользователю
        try {
            FileUtils.copyFile(fileMoveTrash, fileIn);
        } catch (IOException e) {
            System.err.println("При переносе произошла ошибка:");
        }
        // удаляем файл из директории, оставляя в той где нужно пользователю
        fileMoveTrash.delete(); // помоему тут может просто удалиться файл, а при копировании произойти ошибка..
    }

    public void ipCheck(String ipCommand) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(ipCommand);
        Process p = processBuilder.start();
        p.waitFor();
    }

    public void deleteInTrash(String deleteFile) throws IOException {
        File file = new File("C:\\SuperApp\\TrashBin\\" + deleteFile);
        file.delete();
    }
}
