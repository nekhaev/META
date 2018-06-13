package ru.sbrf.bh.bfs.ufs.type.poet;

import com.squareup.javapoet.*;
import ru.sbrf.bh.bfs.generator.literals.Services;
import ru.sbrf.bh.bfs.generator.method.MethodPoet;
import ru.sbrf.bh.bfs.generator.type.service.ServiceTypePoet;

import javax.lang.model.element.Modifier;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public class BfsMonitoringPoet extends ServiceTypePoet{
    @Override
    public String getServiceName() {
        return Services.MONITORING;
    }

    @Override
    protected TypeSpec createType(String param) {
        return TypeSpec.interfaceBuilder(param)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(addAbstractModifier(new AbstractStartMethodPoet().createMethod(param)))
                .addMethod(addAbstractModifier(new AbstractStopMethodPoet().createMethod(param)))
                .build();
    }


    protected static class AbstractStartMethodPoet extends MethodPoet<String> {
        @Override
        protected CodeBlock createMethodBlock(String param) {
            return null;
        }

        @Override
        protected MethodSpec createMethod(String param) {
            MethodSpec.Builder specBuilder = MethodSpec.methodBuilder("start")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.LONG)
                    .addParameter(ClassName.get(String.class),"monitoringSuccessEvent")
                    .addException(Exception.class);
            CodeBlock block = createMethodBlock(param);
            if (block != null)
                specBuilder.addCode(block);
            return specBuilder.build();
        }
    }



    protected static class AbstractStopMethodPoet extends MethodPoet<String> {
        @Override
        protected CodeBlock createMethodBlock(String param) {
            return null;
        }

        @Override
        protected MethodSpec createMethod(String param) {
            MethodSpec.Builder specBuilder =  MethodSpec.methodBuilder("stop")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get(String.class),"monitoringEvent")
                    .addParameter(ClassName.get(String.class),"monitoringMetric")
                    .addParameter(TypeName.LONG,"timeStartProcess")
                    .returns(TypeName.VOID)
                    .addException(Exception.class);
            CodeBlock block = createMethodBlock(param);
            if (block != null)
                specBuilder.addCode(block);
            return specBuilder.build();
        }
    }
}
