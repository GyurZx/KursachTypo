package com.example.kursovayarabotamoiseevrpis11.fileManager;

import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;

public class Cmd {
    private static TextField cmd;

    public static void start(Stage stage, String input) throws IOException, InterruptedException {

        FileManager fileManager = new FileManager("C:\\SuperApp\\"); // Absolutes Path

        //System.out.println("Привет, я файловый менеджер! Если что-то непонятно -> help");

        while (!input.equals(Commands.EXIT)) {
            // cd file.txt x.txt -> tokens[0] = cd, tokens[1] = file.txt, tokens[2] = x.txt
            String tokens[] = input.split(" "); // cd Desktop tokens[0} = cd, tokens[1] = Desktop
            String command = tokens[0];
            switch (command) {
                case Commands.LIST_OF_FILE:
                    fileManager.listOfFile(false);
                    break;
                case Commands.LIST_OF_FILE_WITH_SIZE:
                    fileManager.listOfFile(true);
                    break;
                case Commands.COPY_FILE: {
                    String sourceFileName = tokens[1];
                    String deskFileName = tokens[2];
                    fileManager.copyFile(sourceFileName, deskFileName);
                    break;
                }
                case Commands.CREATE_FILE: {
                    String fileName = tokens[1];
                    fileManager.createFile(fileName);
                    break;
                }
                case Commands.FILE_CONTENT: {
                    String fileName = tokens[1];
                    fileManager.fileContent(fileName);
                    break;
                }
                case Commands.CHANGE_DIRECTORY: {
                    String folderName = tokens[1];
                    fileManager.changeDirectory(folderName);
                    break;
                }
                case Commands.HELP:
                    fileManager.help();
                    break;
                case Commands.DELETE: {
                    String deleteFile = tokens[1];
                    fileManager.delete(deleteFile);
                    break;
                }
                case Commands.RENAME: {
                    String actualFile = tokens[1];
                    String renameFile = tokens[2];
                    fileManager.rename(actualFile, renameFile);
                    break;
                }
                case Commands.SHOW_FILE: {
                    String showFile = tokens[1];
                    fileManager.showFileInfo(showFile);
                    break;
                }
                case Commands.SHOW_PATH_FILE: {
                    fileManager.showPath();
                    break;
                }
                case Commands.CREATE_DIRECTORY: {
                    String createDirectory = tokens[1];
                    fileManager.createDirectoryIn(createDirectory);
                    break;
                }
                case Commands.MOVE_FILE: {
                    String moveFile = tokens[1];
                    String moveInDirectory = tokens[2];
                    fileManager.move(moveFile, moveInDirectory);
                    break;
                }
                case Commands.MOVE_DELETE_IN_TRASH: {
                    String deleteFile = tokens[1];
                    fileManager.deleteTrash(deleteFile);
                    break;
                }
                case Commands.DELETE_IN_TRASH: {
                    String deleteFile = tokens[1];
                    fileManager.deleteInTrash(deleteFile);
                    break;
                }
                case Commands.TRANSITION_DISK: {
                    //String diskName = tokens[1];
                    //FileManager fileManager = new FileManager(diskName + ":\\");
                    //fileManager.TransitionSystemDisk(diskName);
                    break;
                }
                case Commands.APPLICATION_LAUNCH: {
                    String applicationLaunchExe = tokens[1];
                    fileManager.applicationLaunch(applicationLaunchExe);
                    break;
                }
                case Commands.IFCONFIG:
                case Commands.NETSTAT:
                case Commands.SSH: {
                    String ipCommand = tokens[1];
                    fileManager.ipCheck(ipCommand);
                    break;
                }
                default:
                    cmd.setText("Неверная команда:");
                    break;
            }
            break;
        }
    }
}
