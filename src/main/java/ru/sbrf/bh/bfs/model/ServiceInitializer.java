package ru.sbrf.bh.bfs.model;

/**
 * Created by sbt-barsukov-sv on 15.06.2018.
 */
public interface ServiceInitializer<T> {
     T setName(String name);
     T setMonitoringService(String name);
}
