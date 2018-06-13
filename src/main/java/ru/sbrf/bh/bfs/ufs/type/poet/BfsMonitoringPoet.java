package ru.sbrf.bh.bfs.ufs.type.poet;

import com.squareup.javapoet.*;
import ru.sbrf.bh.bfs.generator.type.service.ServiceTypePoet;

import javax.lang.model.element.Modifier;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public class BfsMonitoringPoet extends ServiceTypePoet{
    @Override
    public String getServiceName() {
        return "BfsMonitoringService";
    }

    @Override
    protected TypeSpec createType(String param) {
        return TypeSpec.interfaceBuilder(param)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec.methodBuilder("start")
                        .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                        .returns(TypeName.LONG)
                        .addParameter(ClassName.get(String.class),"monitoringSuccessEvent")
                        .addException(Exception.class)
                        .build())
                .addMethod(MethodSpec.methodBuilder("stop")
                        .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                        .addParameter(ClassName.get(String.class),"monitoringEvent")
                        .addParameter(ClassName.get(String.class),"monitoringMetric")
                        .addParameter(TypeName.LONG,"timeStartProcess")
                        .returns(TypeName.VOID)
                        .addException(Exception.class)
                        .build())
                .build();
    }
}
