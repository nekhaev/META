package ru.sbrf.bh.bfs.generator.type.service;

import com.squareup.javapoet.MethodSpec;
import ru.sbrf.bh.bfs.generator.type.TypePoet;

import javax.lang.model.element.Modifier;
import java.io.File;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public abstract class ServiceTypePoet extends TypePoet<String> {
    public abstract String getServiceName();

    public void makeSimple(String packageName, File outputDir){
        createAdditionalTypes(packageName,outputDir);
        makeSimple(packageName,getServiceName(),outputDir);
    }

    public MethodSpec addAbstractModifier(MethodSpec spec) {
        return spec.toBuilder().addModifiers(Modifier.ABSTRACT).build();
    }

    public abstract void createAdditionalTypes(String packageName, File outputDir);

}
