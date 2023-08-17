module com.example.kursovayarabotamoiseevrpis {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.dynalink;
    requires org.apache.commons.io;
    requires java.datatransfer;
    requires java.desktop;
    requires com.sun.jna;

    opens com.example.kursovayarabotamoiseevrpis11 to javafx.fxml;
    exports com.example.kursovayarabotamoiseevrpis11;
    exports com.example.kursovayarabotamoiseevrpis11.fileManager;
    opens com.example.kursovayarabotamoiseevrpis11.fileManager to javafx.fxml;
    exports com.example.kursovayarabotamoiseevrpis11.functional;
    opens com.example.kursovayarabotamoiseevrpis11.functional to javafx.fxml;
}