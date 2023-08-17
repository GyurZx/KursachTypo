package com.example.kursovayarabotamoiseevrpis11.fileManager;

public interface Commands {
    String LIST_OF_FILE = "ls";
    String LIST_OF_FILE_WITH_SIZE = "ll";
    String COPY_FILE = "cp";
    String CREATE_FILE = "touch";
    String FILE_CONTENT = "cat";
    String CHANGE_DIRECTORY = "cd";
    String EXIT = "exit";
    String DELETE = "rm";
    String HELP = "help";
    String RENAME = "rename";
    String SHOW_FILE = "file";
    String SHOW_PATH_FILE = "pwd";
    String CREATE_DIRECTORY = "mkdir";
    String MOVE_FILE = "mv";
    String DELETE_IN_TRASH = "rtf";
    String MOVE_DELETE_IN_TRASH = "rt";
    String TRANSITION_DISK = "td";
    String APPLICATION_LAUNCH = "launch";
    String IFCONFIG = "ifconfig";
    String NETSTAT = "netstat";
    String SSH = "ssh";
}
