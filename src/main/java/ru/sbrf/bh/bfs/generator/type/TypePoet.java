package ru.sbrf.bh.bfs.generator.type;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import ru.sbrf.bh.bfs.model.Api;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public abstract class TypePoet<T> {

    private static final Logger LOGGER = Logger.getLogger("logger");

    protected abstract TypeSpec createType(T param);

    public void makeSimple(String packageName, T param, File outputDir){
        writeJavaFile(packageName, createType(param),outputDir);
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
