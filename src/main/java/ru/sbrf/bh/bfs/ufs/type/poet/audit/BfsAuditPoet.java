package ru.sbrf.bh.bfs.ufs.type.poet.audit;

import com.squareup.javapoet.*;
import ru.sbrf.bh.bfs.generator.literals.Services;
import ru.sbrf.bh.bfs.generator.method.MethodPoet;
import ru.sbrf.bh.bfs.generator.type.service.ServiceTypePoet;

import javax.lang.model.element.Modifier;
import java.io.File;

/**
 * Created by sbt-barsukov-sv on 15.06.2018.
 */
public class BfsAuditPoet extends ServiceTypePoet {
    @Override
    public String getServiceName() {
        return Services.AUDIT;
    }

    @Override
    protected TypeSpec createType(String param) {
        return TypeSpec.interfaceBuilder(param)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(addAbstractModifier(new AbstractSendMethodPoet().createMethod(param,"send")))
                .build();
    }

    @Override
    public void createAdditionalTypes(String packageName, File outputDir) {
        new BfsAuditEventPoet().makeSimple(packageName,"BfsAuditEvent",outputDir);
    }

    protected static class AbstractSendMethodPoet extends MethodPoet<String> {
        @Override
        protected CodeBlock createMethodBlock(String api) {
            return null;
        }

        @Override
        protected void createDeclaration(String param, MethodSpec.Builder specBuilder) {
            specBuilder.addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.bestGuess("BfsAuditEvent"),"auditEvent")
                    .returns(TypeName.VOID)
                    .addException(Exception.class);
        }
    }
}
