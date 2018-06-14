package ru.sbrf.bh.bfs.generator.enums;


/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public enum ParamFields {
    PROPERTIES ("properties"),
    SERVICES ("services"),
    BUILD ("build"),
    ADAPTERS ("adapters"),

    API ("api"),
    MONITORING_SERVICE("bfsMonitoringService");
    private String field;
    ParamFields(String field) {
        this.field=field;
    }

    public String getField() {
        return field;
    }


}
