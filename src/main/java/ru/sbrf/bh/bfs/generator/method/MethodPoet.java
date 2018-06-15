package ru.sbrf.bh.bfs.generator.method;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public abstract class MethodPoet<T> {

    protected abstract CodeBlock createMethodBlock(T api);
    protected abstract void createDeclaration(T param, MethodSpec.Builder specBuilder);

    protected void addMethodBlock(T param, MethodSpec.Builder specBuilder){

        CodeBlock block = createMethodBlock(param);
        if (block != null)
            specBuilder.addCode(block);
    }

    public  MethodSpec createMethod(T api, String name) {
        MethodSpec.Builder specBuilder =  MethodSpec.methodBuilder(name);
        createDeclaration(api,specBuilder);
        addMethodBlock(api,specBuilder);
        return  specBuilder.build();
    }
}
