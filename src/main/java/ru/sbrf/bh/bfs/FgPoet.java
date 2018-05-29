package ru.sbrf.bh.bfs;

import com.squareup.javapoet.*;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.util.CommonUtil;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class FgPoet {

    private File outputDir;

    public FgPoet(File outputDir) {
        this.outputDir = outputDir;
    }

    public void makeSimple(Api api)  throws IOException {

        ClassName output = api.getFgClass();
        ClassName da = api.getDaClass();
        String daMethod = api.getMethodName();
        ClassName rq = api.getRq();
        ClassName rs = api.getRs();

        String beforeCall = "Before FG call";
        String afterCall = "After FG call";
        String failCall = "Failed FG call";
        String exitCall = "Exit FG call";

        CodeBlock executeBody = CodeBlock.builder()
                .addStatement("LOGGER.debug($S)", beforeCall)
                .addStatement("$T rs = null", rs)
                .beginControlFlow("try")
                    .addStatement("rs = $L.$L(rq)", CommonUtil.serviceBean(da), daMethod)
                    .addStatement("LOGGER.info($S)",afterCall)
                .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("LOGGER.error($S,e)",failCall)
                    .addStatement("throw e")
                .nextControlFlow("finally")
                    .addStatement("LOGGER.info($S)",exitCall)
                .endControlFlow()
                .addStatement("return rs")
                .build();

        MethodSpec execute = MethodSpec.methodBuilder("execute")
                .addParameter(rq, "rq")
                .addModifiers(Modifier.PUBLIC)
                .returns(rs)
                .addCode(executeBody)
                .addException(Exception.class)
                .build();

        TypeSpec fg = TypeSpec.classBuilder(output.simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(execute)
                .addField(CommonUtil.getLogger(output.simpleName()))
                .addField(CommonUtil.beanField(da))
                .build();

        JavaFile javaFile = JavaFile.builder(output.packageName(), fg)
                .indent("    ")
                .build();

        javaFile.writeTo(outputDir);

    }

    public void run(String className) throws IOException {

        ClassName sbrfSmbAccountingRequest = ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.vo.request.legacy",
                "SbrfSmbAccountingRequest");

        CodeBlock executeBody = CodeBlock.builder()
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
