package ru.sbrf.bh.bfs.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import ru.sbrf.bh.bfs.model.Api;

import java.io.File;
import java.io.IOException;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public abstract class TypePoet<T extends Api> {

    protected abstract TypeSpec createType(Api api);
    protected abstract String getPackageName(Api api);

    public void makeSimple(Api api, File outputDir) throws IOException {
        writeJavaFile(getPackageName(api), createType(api),outputDir);
    }

    protected static void writeJavaFile(String packageName, TypeSpec typeSpec, File outputDir)throws IOException {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .indent("    ")
                .build();

        javaFile.writeTo(outputDir);
    }
}
