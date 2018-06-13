package ru.sbrf.bh.bfs.util;

import com.squareup.javapoet.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sbrf.bh.bfs.generator.literals.BeanNames;

import javax.lang.model.element.Modifier;

public class CommonUtil {
    public static FieldSpec getLogger(String className) {
        ClassName loggerFactory = ClassName.get("org.slf4j", "LoggerFactory");

        CodeBlock cb = CodeBlock.of("$T.getLogger($L.class)", loggerFactory, className);

        return FieldSpec.builder(ClassName.get("org.slf4j", "Logger"), "LOGGER")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer(cb)
                .build();
    }

    public static FieldSpec beanField(TypeName serviceName,String beanName) {
        return FieldSpec.builder(serviceName, beanName)
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(Autowired.class)
                .build();
    }

}
