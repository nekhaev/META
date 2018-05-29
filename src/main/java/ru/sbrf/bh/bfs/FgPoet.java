package ru.sbrf.bh.bfs;


import static ru.sbrf.bh.bfs.generator.Statement.*;
import static ru.sbrf.bh.bfs.generator.ControlFlow.*;
import static ru.sbrf.bh.bfs.generator.StringConsts.*;

import com.squareup.javapoet.*;
import ru.sbrf.bh.bfs.generator.MethodPoet;
import ru.sbrf.bh.bfs.generator.TypePoet;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.util.CommonUtil;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class FgPoet extends TypePoet<Api>{

    private static final String BEFORE_FG_CALL = "Before FG call";
    private static final String AFTER_FG_CALL = "After FG call";
    private static final String FAILED_FG_CALL = "Failed FG call";
    private static final String EXIT_FG_CALL = "Exit FG call";

    protected String getPackageName(Api api){
        return api.getFgClass().packageName();
    }

    protected TypeSpec createType(Api api) {
        return TypeSpec.classBuilder(api.getFgClass().simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod((new ExecuteMethodPoet()).createMethod(api,""))
                .addField(CommonUtil.getLogger(api.getFgClass().simpleName()))
                .addField(CommonUtil.beanField(api.getDaClass()))
                .build();
    }

    private static class ExecuteMethodPoet extends MethodPoet<Api> {

        protected MethodSpec createMethod(Api api, String beanName) {
            return MethodSpec.methodBuilder("execute")
                    .addParameter(api.getRq(), RQ)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(api.getRs())
                    .addCode(createMethodBlock(api, beanName))
                    .addException(Exception.class)
                    .build();
        }

        protected CodeBlock createMethodBlock(Api api, String beanName) {
            return CodeBlock.builder()
                    .addStatement(LOGGER_DEBUG_LEVEL, BEFORE_FG_CALL)
                    .addStatement(INITIALIZE_RESPONSE, api.getRs())
                    .beginControlFlow(TRY)
                    .addStatement(FG_UPDATE_RESPONSE, CommonUtil.serviceBean(api.getDaClass()), api.getMethodName())
                    .addStatement(LOGGER_INFO_LEVEL, AFTER_FG_CALL)
                    .nextControlFlow(CATCH, Exception.class)
                    .addStatement(LOGGER_ERROR_LEVEL, FAILED_FG_CALL)
                    .addStatement(EXCEPTION_THROW)
                    .nextControlFlow(FINALLY)
                    .addStatement(LOGGER_INFO_LEVEL, EXIT_FG_CALL)
                    .endControlFlow()
                    .addStatement(RETURN_RESPONSE)
                    .build();
        }

    }



    //TODO проверить
    @Deprecated
    public void run(String className, File outputDir) throws IOException {

        ClassName sbrfSmbAccountingRequest = ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.vo.request.legacy",
                "SbrfSmbAccountingRequest");

        CodeBlock executeBody = CodeBlock.builder()
                //TODO переписать
                .addStatement("LOGGER.debug(LOGGER_MESSAGE_FG_CALL.getMessage(SRV_GET_LEGAL_ACCOUNT_BALANCE, acceptor))")
                .addStatement("SbrfSmbAccountingRequest sbrfSmbAccountingRequest = null")
                .addStatement("SbrfSmbAccountingRequest result = null")
//                .addStatement()
//                .addStatement()
//                .addStatement()
                .build();

        ClassName sbrfGetLegalAccountBalanceAcceptor = ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.acceptor"
                ,"SbrfGetLegalAccountBalanceAcceptor");
        ParameterSpec acceptor = ParameterSpec.builder(sbrfGetLegalAccountBalanceAcceptor,
                "acceptor").build();
        MethodSpec execute = MethodSpec.methodBuilder("execute")
                .addParameter(acceptor)
                .addAnnotation(Override.class)
                .returns(sbrfSmbAccountingRequest)
                .addCode(executeBody)
                .build();

        TypeSpec fg = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(execute)
                .addField(CommonUtil.getLogger(className))
                .addField(FieldSpec.builder(ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.bs", "MonitoringService"), "monitoringService").build())
                .addField(FieldSpec.builder(ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.bs", "PlatformAuditService"), "auditService").build())
                .addSuperinterface(ClassName.get("ru.sbrf.bh", "DataAccess"))
                .build();

        JavaFile javaFile = JavaFile.builder("ru.sbrf.bh.banking.product.smbaccounting.fg", fg)
                .indent("    ")
                .build();

        javaFile.writeTo(new File("C:\\dev\\output\\smbaccounting\\src\\main\\java"));
    }

}
