package com.example.kursovayarabotamoiseevrpis11.functional;

import java.io.File;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.io.FilenameUtils;

public class TabContainer {
    File file;
    StringProperty name;
    StringProperty type;
    StringProperty lastMod;
    StringProperty size;

    public TabContainer(File file){
        this.file = file;
        name = new SimpleStringProperty(file.getName());
        type = new SimpleStringProperty(FilenameUtils.getExtension(file.getName()));
        lastMod = new SimpleStringProperty(new Date(file.lastModified()).toString());
        size = new SimpleStringProperty(String.valueOf(file.length()/1024 + " KB"));
    }

    @Override
    public String toString(){
        return file.getName();
    }

    public String getName(){
        return name.get();
    }
    public String getType(){
        return type.get();
    }
    public String getLastMod(){
        return lastMod.get();
    }
    public String getSize(){
        return size.get();
    }
    public File getFile(){
        return file;
    }
}
