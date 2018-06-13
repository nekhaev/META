package ru.sbrf.bh.bfs.generator.type.api;

import ru.sbrf.bh.bfs.generator.type.TypePoet;
import ru.sbrf.bh.bfs.model.Api;

import java.io.File;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public abstract class ApiTypePoet<T extends  Api> extends TypePoet<Api> {

    protected abstract String getPackageName(T api);

    public void makeSimple(T api,  File outputDir){
        makeSimple(getPackageName(api),api,outputDir);
    }
}
