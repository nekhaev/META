package ru.sbrf.bh.bfs.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import ru.sbrf.bh.bfs.model.Api;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public abstract class TypePoet<T extends Api> {

    private static final Logger LOGGER = Logger.getLogger("logger");

    protected abstract TypeSpec createType(T api);
    protected abstract String getPackageName(T api);

    public void makeSimple(T api, File outputDir){
        writeJavaFile(getPackageName(api), createType(api),outputDir);
    }

    protected static void writeJavaFile(String packageName, TypeSpec typeSpec, File outputDir){
        try {
            JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                    .indent("    ")
                    .build();

            javaFile.writeTo(outputDir);
        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
        }
    }
}
