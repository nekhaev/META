package ru.sbrf.bh.bfs;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DaPoet {

    private File outputDir;

    public DaPoet(File outputDir) {
        this.outputDir = outputDir;
    }

    public void makeSimple(Api api) throws IOException {
        ClassName output=api.getDaClass();
        String daMethodName = api.getMethodName();
        TypeName serviceName = api.getService();
        ClassName rq = api.getRq();
        ClassName rs = api.getRs();

        String beanName = "";

        /*
        отличия разных версий адаптеров
         */
        if(serviceName==null) {

            serviceName = ParameterizedTypeName.get(
                    ClassName.get("ru.sbrf.ufs.integration.module.api.call", "SyncCallClient")
                        ,rq
                        ,rs);
            beanName  = api.getName()+"SyncClient";
        } else {
            beanName = CommonPoems.serviceBean(api.getService());
        }

        String beforeCall = "Before DA call";
        String afterCall = "After DA call";
        String exitCall = "Exit DA call";

        CodeBlock callBody = CodeBlock.builder()
                .addStatement("$T rs = null", rs)
                .addStatement("boolean success = false")
                .beginControlFlow("try")
                    .addStatement("LOGGER.info($S)",beforeCall)
                    .addStatement("rs = $L.sendRequest(new $L<>(rq)).getMessage()"
                            , beanName
                            , ClassName.get("ru.sbrf.ufs.integration.module","RequestDescriptionImpl"))
                    .addStatement("LOGGER.info($S)",afterCall)
                    .addStatement("success = true")
                .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw e")
                .nextControlFlow("finally")
                    .addStatement("LOGGER.info($S)",exitCall)
                .endControlFlow()

                .addStatement("return rs")
                .build();

        MethodSpec call = MethodSpec.methodBuilder(daMethodName)
                .addParameter(rq, "rq")
                .addModifiers(Modifier.PUBLIC)
                .returns(rs)
                .addCode(callBody)
                .addException(Exception.class)
                .build();

        TypeSpec da = TypeSpec.classBuilder(output.simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(call)
                .addField(CommonPoems.getLogger(output.simpleName()))
                .addField(serviceName,beanName)
                .build();

        JavaFile javaFile = JavaFile.builder(output.packageName(), da)
                .indent("    ")
                .build();

        javaFile.writeTo(outputDir);
    }

    public void run(String className) throws IOException {

        ClassName acceptor = ClassName.get("ru.sbrf.bh","Acceptor");
        WildcardTypeName wtn = WildcardTypeName.subtypeOf(ClassName.OBJECT);
        ParameterizedTypeName acceptorWild = ParameterizedTypeName.get(acceptor, wtn);
        ParameterSpec a = ParameterSpec.builder(acceptorWild, "a").build();

        ParameterizedTypeName listObject = ParameterizedTypeName.get(List.class,Object.class);

//        private static final Logger LOGGER = LoggerFactory.getLogger(SbrfGetLegalAccountBalanceDaService.class);

        ClassName sbrfSmbAccountingRequest = ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.vo.request.legacy","SbrfSmbAccountingRequest");
        ClassName legalBalInqRqType = ClassName.get("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto","LegalBalInqRqType");
        ClassName legalBalInqRsType = ClassName.get("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto","LegalBalInqRsType");

        CodeBlock getAllBody = CodeBlock.builder()
                .addStatement("$T request = acceptor.getSbrfSmbAccountingRequest()", sbrfSmbAccountingRequest)
                .addStatement("$T rq = null", legalBalInqRqType)
                .addStatement("$T rs = null", legalBalInqRsType)
                .addStatement("boolean success = false")
                .beginControlFlow("try")
                    .addStatement("rq = GetLegalAccountBalanceRqMapper.map(request)")
                    .addStatement("LOGGER.info(LOGGER_MESSAGE_INTEGRATION_ADAPTER_CALL.getMessage(BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE))")
                    .addStatement("rs = srvGetLegalAccountBalanceSyncClient.sendRequest(new RequestDescriptionImpl<>(rq)).getMessage()")
                    .addStatement("LOGGER.info(LOGGER_MESSAGE_INTEGRATION_ADAPTER_RETURN.getMessage(BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE))")
                    .addStatement("GetLegalAccountBalanceRsMapper.map(rs, request)")
                    .addStatement("success = true")
                .nextControlFlow("catch ($T e)", ExecutionException.class)
                .addStatement("throw e")
                .nextControlFlow("catch ($T e)", ClassName.get("ru.sbrf.ufs.integration.module.exception","IntegrationException"))
                    .addStatement("LOGGER.error(LOGGER_MESSAGE_INTEGRATION_ADAPTER_ERROR.getMessage(BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE), e)")
                    .addStatement("throw ExecutionExceptionMapper.getExecutionException(e)")
                .nextControlFlow("catch ($T e)", Exception.class)
                    .addStatement("throw new ExecutionException(INTERNAL_ERROR, e)")
                .nextControlFlow("finally")
                    .addStatement("AuditEventBsService.audit(auditService, UFS_ESB_GETLEGALACCOUNTBALANCESERVICE, rq, rs, success)")
                    .addStatement("MonitoringBsService.notifyEvent(monitoringService, MonitoringEvent.SRV_GET_LEGAL_ACCOUNT_BALANCE.getEvent(success))")
                    .addStatement("LOGGER.debug(LOGGER_MESSAGE_DA_EXIT.getMessage($S, BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE))", "getAll")
                .endControlFlow()
                .addStatement("return Collections.<Object>singletonList(request)")

                .build();

        MethodSpec getAll = MethodSpec.methodBuilder("getAll")
                .addParameter(a)
                .addAnnotation(Override.class)
                .returns(listObject)
                .addCode(getAllBody)
                .build();

        TypeSpec da = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(getAll)
                .addField(CommonPoems.getLogger(className))
                .addField(FieldSpec.builder(ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.bs", "MonitoringService"), "monitoringService").build())
                .addField(FieldSpec.builder(ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.bs", "PlatformAuditService"), "auditService").build())
                .addSuperinterface(ClassName.get("ru.sbrf.bh", "DataAccess"))
                .build();

        JavaFile javaFile = JavaFile.builder("ru.sbrf.bh.banking.product.smbaccounting.da", da)
                .indent("    ")
                .build();

        javaFile.writeTo(new File("C:\\dev\\output\\smbaccounting\\src\\main\\java"));
    }
}
