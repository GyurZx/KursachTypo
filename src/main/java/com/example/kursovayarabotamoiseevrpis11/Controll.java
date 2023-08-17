package com.example.kursovayarabotamoiseevrpis11;

import com.example.kursovayarabotamoiseevrpis11.fileManager.Cmd;
import com.example.kursovayarabotamoiseevrpis11.functional.*;
import com.example.kursovayarabotamoiseevrpis11.functional.Window;
import com.sun.jna.Library;
import com.sun.jna.Native;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.datatransfer.Clipboard;
import java.io.*;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javafx.scene.control.Button;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controll {

    private Stage stage;
    private Scene scene;
    private Parent parent;

    final Path root = new File("C:\\SuperApp").toPath();

    Path currentFolder = root;

    @FXML
    List<Path> lastOpened = new ArrayList<>();

    @FXML
    private VBox VBoxViewList;

    @FXML
    private Button BAllProcess;

    @FXML
    private MenuItem About;

    @FXML
    private Button bcmd;

    @FXML
    private Button bnext;

    @FXML
    private Button bdirectory;

    @FXML
    private Button blinux;

    @FXML
    private Button bmonresource;

    @FXML
    private Button bpanel;

    @FXML
    private TextField cmd;

    @FXML
    private TextArea cmd_info;

    @FXML
    private Menu id_context_menu;

    @FXML
    private Button bBack, bForward, bRefresh;

    @FXML
    private TextField directory;

    @FXML
    private TableColumn<TabContainer, String> lastModColumn, nameColumn, sizeColumn, typeColumn;

    @FXML
    private TableView<TabContainer> tableView;

    @FXML
    private TreeView<TabContainer> treeView;

    @FXML
    void out_directory(ActionEvent event) {
        String file = directory.getText();
        Path start = Paths.get("C:\\SuperApp\\");
        try (Stream<Path> stream = Files.walk(start, Integer.MAX_VALUE)) {
            List<String> fileNames = stream
                    .map(String::valueOf)
                    .filter(path -> path.endsWith(file))
                    .collect(Collectors.toList());
            if (fileNames.isEmpty()) {
                System.out.println("No files found");
                Alert winMessage = new Alert(Alert.AlertType.ERROR);
                winMessage.setTitle("Information");
                winMessage.setHeaderText("No files found");
                winMessage.setContentText("");
                winMessage.showAndWait();
            } else {
                System.out.println("Found " + fileNames.size() + " files:");
                for (String fileName : fileNames) {
                    System.out.println(fileName);
                }

                String message = "Found " + fileNames.size() + " files:\n";
                for (String fileName : fileNames) {
                    message += fileName + "\n";
                }
                JOptionPane.showMessageDialog(null, message, "File Names", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void insert_directory() {

    }

    @FXML
    public void saveFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            ProcessLogger processLogger = new ProcessLogger(selectedFile);
            processLogger.AllLog();
        }
    }

    public interface CLibrary extends Library {
        SystemCommand.CLibrary INSTANCE = (SystemCommand.CLibrary) Native.load("c", SystemCommand.CLibrary.class);
        int system(String cmd);
    }

    @FXML
    void processLogger(ActionEvent event) {
        ProcessLogger processLogger = new ProcessLogger();
        processLogger.AllLog();
        Alert winMessage = new Alert(Alert.AlertType.INFORMATION);
        winMessage.setTitle("Information");
        winMessage.setHeaderText("Все процессы OC записаны в лог-файл");
        winMessage.showAndWait();
    }

    @FXML
    void writeFile(ActionEvent event) {

    }

    @FXML
    void readFile(ActionEvent event) {

    }

    @FXML
    void AllProcess(ActionEvent event) throws Exception {
        ProcessChecker processChecker = new ProcessChecker();
        Stage showP = new Stage();
        processChecker.start(showP);
    }

    @FXML
    void FileShow(ActionEvent event) {
        Window window = new Window();
        Stage showWindow = new Stage();
        window.start(showWindow);
    }

    @FXML
    void FunctionShow(ActionEvent event) throws IOException, InterruptedException {
        ProcessWriterInLog processWriterInLog = new ProcessWriterInLog();
        Process p = Runtime.getRuntime().exec("cmd /c start netstat.exe");
        //SystemCommand.CLibrary.INSTANCE.system("command");
        processWriterInLog.log("netstat.exe");
        p.waitFor();
    }

    @FXML
    void onBack(ActionEvent event) {
        if(!currentFolder.endsWith(root)){
            lastOpened.add(currentFolder);
            currentFolder = currentFolder.getParent();
            onRefresh(event);
            bForward.setDisable(false);
        }
        else bBack.setDisable(true);
    }

    @FXML
    void onForward(ActionEvent event) {

        bBack.setDisable(false);
        if(lastOpened.size() == 0){
            bForward.setDisable(true);
        }
        else if(lastOpened.size() == 1){
            currentFolder = lastOpened.get(0);
            onRefresh(event);
            lastOpened.remove(0);
            bForward.setDisable(true);
        }
        else {
            currentFolder = lastOpened.get(lastOpened.size()-1);
            onRefresh(event);
            lastOpened.remove(lastOpened.size()-1);
        }
    }

    @FXML
    void onRefresh(ActionEvent event) {
        Node node = (Node) event.getSource();
        Scene scene = node.getScene();

        tableView = (TableView<TabContainer>) scene.lookup("#tableView");
        openTable(currentFolder.toFile(), tableView);

        init();
        treeView.setRoot(new TreeItem<>(new TabContainer(root.toFile())));
        addNode(treeView.getRoot(), root.toFile());
    }

    class ImageTransferable implements Transferable {

        private File fileDragAndDrop;
        private Image theImage;

        public ImageTransferable(Image image) {
            theImage = image;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(DataFlavor.imageFlavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if(flavor.equals(DataFlavor.imageFlavor)) {
                return theImage;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }
    void init(){
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    currentFolder = Path.of((tableView.getSelectionModel().getSelectedItem().getFile().getPath()));

                    if(!currentFolder.toFile().isDirectory()){
                        //System.out.println(selDir.getParent());
                        currentFolder = currentFolder.getParent();
                    }

                    openTable(currentFolder.toFile(), tableView);

                }
            }
        });

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem itemFolder = new MenuItem("folder"); itemFolder.setOnAction(event -> {
            File file = tableView.getSelectionModel().getSelectedItem().getFile();
            String renamefile = String.valueOf(file);
            int startLastFolderPosition = renamefile.lastIndexOf("\\"); // root\\documents\\ <- sub
            renamefile = renamefile.substring(0, startLastFolderPosition) + "\\" + directory.getText(); // root\\documents\\
            File createDir = new File(renamefile);
            createDir.mkdir();
        });
        MenuItem itemFile = new MenuItem("file"); itemFile.setOnAction(event -> {
            File file = tableView.getSelectionModel().getSelectedItem().getFile();
            String renamefile = String.valueOf(file);
            int startLastFolderPosition = renamefile.lastIndexOf("\\"); // root\\documents\\ <- sub
            renamefile = renamefile.substring(0, startLastFolderPosition) + "\\" + directory.getText(); // root\\documents\\
            File createFile = new File(renamefile);
            try {
                createFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Menu itemCreate = new Menu("create");
        itemCreate.getItems().addAll(itemFolder, itemFile);
        contextMenu.getItems().addAll(itemCreate);

        MenuItem cut = new MenuItem("cut"); cut.setOnAction(event -> { // код для вырезания элемента
            File file = tableView.getSelectionModel().getSelectedItem().getFile();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
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
            file.delete();
        });
        MenuItem copy = new MenuItem("copy");
        copy.setOnAction(event -> {
            File file = tableView.getSelectionModel().getSelectedItem().getFile();
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
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
        });

//        @FXML
//        MenuItem copy = new MenuItem("copy");
//        copy.setOnAction(event -> {
//            File file = tableView.getSelectionModel().getSelectedItem().getFile();
//            String filePath = file.getAbsolutePath();
//            String command = "xclip -selection clipboard -t image/png -i " + filePath;
//            try {
//                ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
//                builder.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

        MenuItem paste = new MenuItem("paste");
        paste.setOnAction(event -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            if (clipboard.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
                try {
                    List<File> files = (List<File>) clipboard.getData(DataFlavor.javaFileListFlavor);
                    for (File file : files) {
                        // Move the file to the table view tabContainer
                        TabContainer fileInTab = new TabContainer(file);
                        tableView.getItems().add(fileInTab);
                        // Move the file to the directory with a new name
                        File destination = new File("C:\\SuperApp\\" + file.getName());
                        FileUtils.copyFile(file, destination);
                    }
                } catch (UnsupportedFlavorException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        MenuItem open = new MenuItem("open"); open.setOnAction(event -> { // код для вставки элемента
            File file = tableView.getSelectionModel().getSelectedItem().getFile();
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        MenuItem delete = new MenuItem("delete"); delete.setOnAction(event -> { // код для удаления элемента
            File file = tableView.getSelectionModel().getSelectedItem().getFile();
            System.out.println(file);
            String check = String.valueOf(file);
            System.out.println(check);
            if(check.equals("C:\\SuperApp\\System")) {
                Alert winMessage = new Alert(Alert.AlertType.ERROR);
                winMessage.setTitle("Information");
                winMessage.setHeaderText("Нельзя удалить системную папку");
                winMessage.setContentText("");
                winMessage.showAndWait();
            } else {
                file.delete();
            }
        });
        MenuItem rename = new MenuItem("rename"); rename.setOnAction(event -> { // код для переименования элемента
            // Проверить
            File file = tableView.getSelectionModel().getSelectedItem().getFile();
            String renamefile = String.valueOf(file);
            System.out.println(renamefile);
            if(renamefile.equals("C:\\SuperApp\\System")) {
                Alert winMessage = new Alert(Alert.AlertType.ERROR);
                winMessage.setTitle("Information");
                winMessage.setHeaderText("Нельзя переименовать системную папку");
                winMessage.setContentText("");
                winMessage.showAndWait();
            } else {
                int startLastFolderPosition = renamefile.lastIndexOf("\\"); // root\\documents\\ <- sub
                renamefile = renamefile.substring(0, startLastFolderPosition) + "\\" + directory.getText(); // root\\documents\\
                file.renameTo(new File(renamefile));
            }
        });
        itemFolder.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        itemFile.setAccelerator(KeyCombination.keyCombination("Ctrl+1"));
        cut.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        copy.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        paste.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        delete.setAccelerator(KeyCombination.keyCombination("Delete"));
        rename.setAccelerator(KeyCombination.keyCombination("F2"));

        contextMenu.getItems().addAll(copy, cut, open, paste, delete, rename);
        // Пример использования контекстного меню на конкретном элементе Node node = ...;
        // элемент, на котором будет работать контекстное меню node.setOnContextMenuRequested(event -> { contextMenu.show(node, event.getScreenX(), event.getScreenY()); });


        tableView.setContextMenu(contextMenu);
        tableView.setRowFactory(tv -> {
            TableRow<TabContainer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
                    TabContainer selectedData = row.getItem();
                }
            });
            return row;
        });

        tableView.setRowFactory(tv -> {
            TableRow<TabContainer> row = new TableRow<>();

//            row.setOnMouseClicked(event -> {
//                File selectedFile;
//                if(Objects.isNull(row.getItem()))
//                    factory.setFile(curDir.toFile());
//                else{
//                    selectedFile = row.getItem().getFile();
//                    factory.setFile(selectedFile);
//                }
//                if (event.getButton() == MouseButton.SECONDARY) {
//                    contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
//
//                }
//            });

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Dragboard db = row.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    List<File> files = new ArrayList<>();
                    files.add(row.getItem().getFile());
                    content.putFiles(files);
                    db.setContent(content);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();

                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    File source = db.getFiles().get(db.getFiles().size()-1);

                    File dest;
                    Path curDir = Path.of("C:\\SuperApp\\");
                    System.out.println(row.getIndex());
                    if(!Objects.isNull(row.getItem()))
                        dest = row.getItem().getFile();
                    else dest = curDir.toFile();

                    System.out.println(source + "\n" + dest + "\n" + new File(dest.toString() + "/" + source.getName()));

                    if (source.renameTo(new File(dest.toString() + "/" + source.getName())))
                        directory.setText("File " + source.getName() + " successfully drag and drop into " + dest.getName());
                    else directory.setText("Error in drag and drop");
                    event.setDropCompleted(true);
                    event.consume();
                    //refresh();

                }
            });
            return row;
        });
    }

    private void openTable(File selDir, TableView<TabContainer> tableView){

        directory.setText(new File("C:\\" + root.relativize(currentFolder)).getPath());


        File[] files = selDir.listFiles();
        List<TabContainer> tabs = new ArrayList<>();
        for (File file : Objects.requireNonNull(files)) {
            tabs.add(new TabContainer(file));
        }

        tableView.setItems(FXCollections.observableList(tabs));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        lastModColumn.setCellValueFactory(new PropertyValueFactory<>("LastMod"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("Size"));

        tableView.refresh();
    }

    @FXML
    private void addNode(TreeItem<TabContainer> parentNode, File file) {
        TabContainer container = new TabContainer(file);
        TreeItem<TabContainer> node = new TreeItem<>(container);
        parentNode.getChildren().add(node);

        if (file.isDirectory()) {
            for (File subFile : Objects.requireNonNull(file.listFiles())) {
                addNode(node, subFile);
            }
        }
    }
    @FXML
    void OnDelete(ActionEvent event) {
        File file = tableView.getSelectionModel().getSelectedItem().getFile();
        File trash = new File("C:\\SuperApp\\TrashBin\\");
        moveFile(file, trash);
    }
    @FXML
    void onRename(ActionEvent event) {

    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if(event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    private void handleDrop(DragEvent event) throws FileNotFoundException {
        List<File> files = event.getDragboard().getFiles();
        // Короче, тут скидываем файл и из потока его кладем в таблицу (доделать)
        System.out.println(files);
//        File fileTable = tableView.getSelectionModel().getSelectedItem().getFile();
//        File trash = new File("C:\\SuperApp\\TrashBin\\");
//        moveFile((File) files, fileTable);
    }
    void moveFile(File source, File dest){
        source.renameTo(new File(dest.toString() + "\\" + source.getName()));
    }
    @FXML
    void cmd_command_panel(ActionEvent event) {

    }

    @FXML
    void next(ActionEvent event) throws IOException {
            Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view2.fxml")));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader);
            stage.setScene(scene);
            stage.show();
    }

    @FXML
    void back(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void consuld_save(ActionEvent event) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("notepad.exe");
        //SystemCommand.CLibrary.INSTANCE.system("command");
        Process p = pb.start();
        ProcessWriterInLog processWriterInLog = new ProcessWriterInLog();
        processWriterInLog.log("notepad.exe");
        File fileSave = new File("C:\\SuperApp\\Bin\\consoleLog.txt");
        fileSave.createNewFile();
        InputStream is = p.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        p.waitFor();

//        //ProcessBuilder pb = new ProcessBuilder("notepad.exe");
//        //Process p = pb.start();
//        //File fileSave = new File("C:\\SuperApp\\Bin\\consoleLog.txt");
//        FileWriter fileWriter = new FileWriter("consoleLog.txt", false);
//        String command = cmd.getText();
//        fileWriter.write(command);
//        //p.waitFor();
//
//        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
//        System.out.println("System architecture: " + osBean.getArch());
//        System.out.println("Available processors: " + osBean.getAvailableProcessors());
//        System.out.println("System name: " + osBean.getName());
//        System.out.println("System version: " + osBean.getVersion());
    }

    @FXML
    void open_parament(ActionEvent event) throws IOException {
//        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "diskpart", "list volume");
//        Process process = builder.start();
//        BufferedReader reader =
//                new BufferedReader(new InputStreamReader(process.getInputStream()));
//        String line;
//        while ((line = reader.readLine()) != null) {
//            if (line.contains("Removable")) {
//                // найден съемный носитель
//                // обработка данных о носителе
//            }
//        }
    }

    @FXML
    void print_high(ActionEvent event) {

    }

    @FXML
    void print_low(ActionEvent event) {

    }

    @FXML
    void open_provodnick(ActionEvent event) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("cmd /c explorer C:\\");
        //SystemCommand.CLibrary.INSTANCE.system("command");
        ProcessWriterInLog processWriterInLog = new ProcessWriterInLog();
        processWriterInLog.log("explorer");
    }

    @FXML
    void open_notepad(ActionEvent event) throws IOException, InterruptedException {
        //Process p = Runtime.getRuntime().exec("cmd /c notepad");
        //SystemCommand.CLibrary.INSTANCE.system("command");
        ProcessWriterInLog processWriterInLog = new ProcessWriterInLog();
        processWriterInLog.log("notepad.exe");
    }

    @FXML
    void open_cmd_panel(ActionEvent event) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("cmd /c start cmd.exe");
        //SystemCommand.CLibrary.INSTANCE.system("command");
        ProcessWriterInLog processWriterInLog = new ProcessWriterInLog();
        processWriterInLog.log("cmd.exe");
    }

    @FXML
    void hotkeys(ActionEvent event) {
        Alert winMessage = new Alert(Alert.AlertType.INFORMATION);
        winMessage.setTitle("Information");
        winMessage.setHeaderText("Create Folder Ctrl+0 \nCreate File Ctrl+1 \nCut Ctrl+X \nCopy Ctrl+C \nPaste Ctrl+V \nDelete Delete \nRename F2");
        winMessage.setContentText("Выполнил: Моисеев М.Е. РПИС-11");
        winMessage.showAndWait();
    }

    @FXML
    void Info_study(ActionEvent event) {
        Alert winMessage = new Alert(Alert.AlertType.INFORMATION);
        winMessage.setTitle("Information");
        winMessage.setHeaderText("OC Linux, ЯП: Java");
        winMessage.setContentText("Выполнил: Моисеев М.Е. РПИС-11");
        winMessage.showAndWait();
    }

    @FXML
    void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void paneluprav(ActionEvent event) {
        try {
            Process p = Runtime.getRuntime().exec("cmd /c start control");
            //SystemCommand.CLibrary.INSTANCE.system("command");
            ProcessWriterInLog processWriterInLog = new ProcessWriterInLog();
            processWriterInLog.log("control");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void print_command(ActionEvent event) throws IOException, InterruptedException {
        Cmd command = new Cmd();
        Stage showP = new Stage();
        command.start(showP, cmd.getText());
    }

    @FXML
    void printresource(ActionEvent event) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("cmd /c start netstat.exe");
        //SystemCommand.CLibrary.INSTANCE.system("command");
        ProcessWriterInLog processWriterInLog = new ProcessWriterInLog();
        processWriterInLog.log("netstat.exe");
    }

    @FXML
    void terminalLinux(ActionEvent event) {
        try {
            //SystemCommand.CLibrary.INSTANCE.system("cmd /c start cmd");
            Process p = Runtime.getRuntime().exec("cmd /c start cmd");
            ProcessWriterInLog processWriterInLog = new ProcessWriterInLog();
            processWriterInLog.log("cmd /c start cmd");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void gitrederect(ActionEvent event) {
        String url = "https://github.com";
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void fleshShow(ActionEvent event) throws Exception {
        String disk = directory.getText();
        FlashDriveExplorer flashDriveExplorer = new FlashDriveExplorer();
        Stage showWindow = new Stage();
        flashDriveExplorer.start(showWindow);
    }

//    @FXML
//    void fleshShow(ActionEvent event) throws Exception {
//        String disk = directory.getText();
//        RemovableMedia removableMedia = new RemovableMedia(disk);
//        Stage showWindow = new Stage();
//        removableMedia.start(showWindow);
//    }

    @FXML
    void style1(ActionEvent event) {

        Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);

        bcmd.setStyle("-fx-background-color : #778899;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        bdirectory.setStyle("-fx-background-color : #778899;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        blinux.setStyle("-fx-background-color : #778899;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        bpanel.setStyle("-fx-background-color : #778899;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        bmonresource.setStyle("-fx-background-color : #778899;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        directory.setStyle("-fx-background-color : #778899;");

        cmd.setStyle("-fx-background-color : #C0C0C0;");

    }

    @FXML
    void style2() {

        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);

        bcmd.setStyle("-fx-background-color : #8B00FF;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        bdirectory.setStyle("-fx-background-color : #8B00FF;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        blinux.setStyle("-fx-background-color : #8B00FF;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        bpanel.setStyle("-fx-background-color : #8B00FF;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        bmonresource.setStyle("-fx-background-color : #8B00FF;\n" +
                "    -fx-background-radius : 30 30 30 30;");

        directory.setStyle("-fx-background-color : #778899;");

        cmd.setStyle("-fx-background-color : #F3F3F3;");

    }
}