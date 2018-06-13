package ru.sbrf.bh.bfs.ufs.type.poet;

import com.squareup.javapoet.*;
import ru.sbrf.bh.bfs.generator.literals.ControlFlow;
import ru.sbrf.bh.bfs.generator.method.MethodPoet;
import ru.sbrf.bh.bfs.generator.type.service.ServiceTypePoet;
import ru.sbrf.bh.bfs.util.CommonUtil;

import javax.lang.model.element.Modifier;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public class BfsMonitoringPoetImpl extends ServiceTypePoet {
    @Override
    protected TypeSpec createType(String param) {
        return TypeSpec.classBuilder(param)
                .addSuperinterface(ClassName.get("","BfsMonitoringService"))
                .addModifiers(Modifier.PUBLIC)
                .addMethod((new StartMethodPoet()).createMethod(param,""))
                .addMethod((new StopMethodPoet()).createMethod(param,""))
                .addField(CommonUtil.getLogger(param))
                .addField(FieldSpec.builder(ClassName.get("ru.sbrf.ufs.platform.monitoring", "MonitoringService"), "monitoringService").build())
                .build();
    }


    private static class StartMethodPoet extends MethodPoet<String> {
        @Override
        protected CodeBlock createMethodBlock(String param, String beanName) {
            return CodeBlock.builder()
                    .beginControlFlow(ControlFlow.TRY)
                    .addStatement("monitoringService.notifyEvent(monitoringSuccessEvent)")
                    .nextControlFlow(ControlFlow.CATCH, Exception.class)
                    .endControlFlow()
                    .addStatement("return System.currentTimeMillis()").build();
        }

        @Override
        protected MethodSpec createMethod(String param, String beanName) {
            return MethodSpec.methodBuilder("start")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.LONG)
                    .addCode(createMethodBlock(param,beanName))
                    .addParameter(ClassName.get(String.class),"monitoringSuccessEvent")
                    .addException(Exception.class)
                    .build();
        }
    }

   private static class StopMethodPoet extends MethodPoet<String> {
       @Override
       protected CodeBlock createMethodBlock(String param, String beanName) {
           return CodeBlock.builder()
                   .beginControlFlow(ControlFlow.TRY)
                   .addStatement("monitoringService.notifyEvent(monitoringEvent)")
                   .addStatement("monitoringService.notifyMetric(monitoringMetric,(double)(System.currentTimeMillis() - timeStartProcess))")
                   .nextControlFlow(ControlFlow.CATCH, Exception.class)
                   .endControlFlow().build();
       }

       @Override
       protected MethodSpec createMethod(String param, String beanName) {
           return MethodSpec.methodBuilder("stop")
                   .addModifiers(Modifier.PUBLIC)
                   .addParameter(ClassName.get(String.class),"monitoringEvent")
                   .addParameter(ClassName.get(String.class),"monitoringMetric")
                   .addParameter(TypeName.LONG,"timeStartProcess")
                   .returns(TypeName.VOID)
                   .addCode(createMethodBlock(param,beanName))
                   .addException(Exception.class)
                   .build();
       }
   }

    @Override
    public String getServiceName() {
        return "BfsMonitoringServiceImpl";
    }
}
