package ru.sbrf.bh.bfs.generator.type.service;

import ru.sbrf.bh.bfs.generator.type.TypePoet;

import java.io.File;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public abstract class ServiceTypePoet extends TypePoet<String> {
    public abstract String getServiceName();

    public void makeSimple(String packageName, File outputDir){
        makeSimple(packageName,getServiceName(),outputDir);
    }
}
