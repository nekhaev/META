package ru.sbrf.bh.bfs.generator.method;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public abstract class MethodPoet<T> {

    protected abstract CodeBlock createMethodBlock(T api);
    protected abstract MethodSpec createMethod(T api);
}
