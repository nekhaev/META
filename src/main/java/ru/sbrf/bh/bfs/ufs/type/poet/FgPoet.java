package ru.sbrf.bh.bfs.ufs.type.poet;


import com.squareup.javapoet.*;
import ru.sbrf.bh.bfs.generator.enums.ApiFields;
import ru.sbrf.bh.bfs.generator.literals.BeanNames;
import ru.sbrf.bh.bfs.generator.literals.ControlFlow;
import ru.sbrf.bh.bfs.generator.literals.Statement;
import ru.sbrf.bh.bfs.generator.method.MethodPoet;
import ru.sbrf.bh.bfs.generator.type.api.ApiTypePoet;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.util.CommonUtil;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class FgPoet extends ApiTypePoet<Api> {

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
                .addMethod((new ExecuteMethodPoet()).createMethod(api,"execute"))
                .addField(CommonUtil.getLogger(api.getFgClass().simpleName()))
                .addField(CommonUtil.beanField(api.getDaClass(), BeanNames.DATA_ACCESS_BEAN))
                .build();
    }

    private static class ExecuteMethodPoet extends MethodPoet<Api> {


        @Override
        protected void createDeclaration(Api param, MethodSpec.Builder specBuilder) {
            specBuilder.addParameter(param.getRq(), ApiFields.RQ.getField())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(param.getRs())
                    .addException(Exception.class);
        }

        protected CodeBlock createMethodBlock(Api api) {
            return CodeBlock.builder()
                    .addStatement(Statement.LOGGER_DEBUG_LEVEL, BEFORE_FG_CALL)
                    .addStatement(Statement.INITIALIZE_RESPONSE, api.getRs())
                    .beginControlFlow(ControlFlow.TRY)
                    .addStatement(Statement.FG_UPDATE_RESPONSE, BeanNames.DATA_ACCESS_BEAN, api.getMethodName())
                    .addStatement(Statement.LOGGER_INFO_LEVEL, AFTER_FG_CALL)
                    .nextControlFlow(ControlFlow.CATCH, Exception.class)
                    .addStatement(Statement.LOGGER_ERROR_LEVEL, FAILED_FG_CALL)
                    .addStatement(Statement.EXCEPTION_THROW)
                    .nextControlFlow(ControlFlow.FINALLY)
                    .addStatement(Statement.LOGGER_INFO_LEVEL, EXIT_FG_CALL)
                    .endControlFlow()
                    .addStatement(Statement.RETURN_RESPONSE)
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
