package ru.sbrf.bh.bfs.generator.enums;

import ru.sbrf.bh.bfs.model.Api;

import java.lang.reflect.Method;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public enum ParamFields {
    PROPERTIES ("properties"),
    SERVICES ("services"),
    BUILD ("build"),
    ADAPTERS ("adapters"),

    API ("api");
    private String field;
    ParamFields(String field) {
        this.field=field;
    }

    public String getField() {
        return field;
    }


}
