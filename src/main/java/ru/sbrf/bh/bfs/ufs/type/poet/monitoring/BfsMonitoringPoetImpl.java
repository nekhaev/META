package ru.sbrf.bh.bfs.ufs.type.poet.monitoring;

import com.squareup.javapoet.*;
import ru.sbrf.bh.bfs.generator.literals.ControlFlow;
import ru.sbrf.bh.bfs.generator.literals.Services;
import ru.sbrf.bh.bfs.util.CommonUtil;

import javax.lang.model.element.Modifier;
import java.io.File;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public class BfsMonitoringPoetImpl extends BfsMonitoringPoet {
    @Override
    protected TypeSpec createType(String param) {
        return TypeSpec.classBuilder(param)
                .addSuperinterface(ClassName.get("", Services.MONITORING))
                .addModifiers(Modifier.PUBLIC)
                .addMethod((new StartMethodPoet()).createMethod(param,"start"))
                .addMethod((new StopMethodPoet()).createMethod(param,"stop"))
                .addField(CommonUtil.getLogger(param))
                .addField(CommonUtil.beanField(ClassName.get("ru.sbrf.ufs.platform.monitoring", "MonitoringService"), "monitoringService"))
                .build();
    }


    private static class StartMethodPoet extends BfsMonitoringPoet.AbstractStartMethodPoet {
        @Override
        protected CodeBlock createMethodBlock(String param) {
            return CodeBlock.builder()
                    .beginControlFlow(ControlFlow.TRY)
                    .addStatement("monitoringService.notifyEvent(monitoringSuccessEvent)")
                    .nextControlFlow(ControlFlow.CATCH, Exception.class)
                    .endControlFlow()
                    .addStatement("return System.currentTimeMillis()").build();
        }
    }

   private static class StopMethodPoet extends BfsMonitoringPoet.AbstractStopMethodPoet {
       @Override
       protected CodeBlock createMethodBlock(String param) {
           return CodeBlock.builder()
                   .beginControlFlow(ControlFlow.TRY)
                   .addStatement("monitoringService.notifyEvent(monitoringEvent)")
                   .addStatement("monitoringService.notifyMetric(monitoringMetric,(double)(System.currentTimeMillis() - timeStartProcess))")
                   .nextControlFlow(ControlFlow.CATCH, Exception.class)
                   .endControlFlow().build();
       }
   }

    @Override
    public String getServiceName() {
        return Services.MONITORING_IMPL;
    }

    /**
     * Иначе улетим в рекурсию
     **/
    @Override
    public void createAdditionalTypes(String packageName, File outputDir){}
}
