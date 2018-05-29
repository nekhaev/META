package ru.sbrf.bh.bfs.generator;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import ru.sbrf.bh.bfs.model.Api;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public abstract class MethodPoet<T extends Api> {

    protected abstract CodeBlock createMethodBlock(T api, String beanName);
    protected abstract MethodSpec createMethod(T api, String beanName);
}
