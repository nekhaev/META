package ru.sbrf.bh.bfs.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

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

    public static FieldSpec beanField(ClassName serviceName) {
        return FieldSpec.builder(serviceName, serviceBean(serviceName))
                .addModifiers(Modifier.PRIVATE)
                .build();
    }

    public static String serviceBean(ClassName serviceName) {
        String simpleServiceName = serviceName.simpleName();
        return simpleServiceName.substring(0, 1).toLowerCase() + simpleServiceName.substring(1);
    }


}
