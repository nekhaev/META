package ru.sbrf.bh.bfs;

import static ru.sbrf.bh.bfs.generator.Statement.*;
import static ru.sbrf.bh.bfs.generator.ControlFlow.*;

import com.squareup.javapoet.*;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.util.CommonUtil;
import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DaPoet {

    private final static String BEFORE_DA_CALL = "Before DA call";
    private final static String AFTER_DA_CALL = "After DA call";
    private final static String EXIT_DA_CALL = "Exit DA call";

    private File outputDir;

    public DaPoet(File outputDir) {
        this.outputDir = outputDir;
    }

    public void makeSimple(Api api) throws IOException {
        /*
        отличия разных версий адаптеров
         */
        TypeName serviceName = api.getService() != null ? api.getService() :
                                                                    ParameterizedTypeName.get(
                                                                    ClassName.get("ru.sbrf.ufs.integration.module.api.call", "SyncCallClient")
                                                                    ,api.getRq()
                                                                    ,api.getRs());
        String beanName = api.getService() != null ? CommonUtil.serviceBean(api.getService()) : api.getName()+"SyncClient";

        CodeBlock callBody = CodeBlock.builder()
                .addStatement(INITIALIZE_RESPONSE, api.getRs())
                .addStatement(INITIALIZE_SUCCESS_FLAG)
                .beginControlFlow(TRY)
                    .addStatement(LOGGER_INFO_LEVEL, BEFORE_DA_CALL)
                    .addStatement("rs = $L.sendRequest(new $L<>(rq)).getMessage()"
                            , beanName
                            , ClassName.get("ru.sbrf.ufs.integration.module","RequestDescriptionImpl"))
                    .addStatement(LOGGER_INFO_LEVEL, AFTER_DA_CALL)
                    .addStatement(CHANGE_SUCCESS_FLAG)
                .nextControlFlow(CATCH, Exception.class)
                    .addStatement(EXCEPTION_THROW)
                .nextControlFlow(FINALLY)
                    .addStatement(LOGGER_INFO_LEVEL, EXIT_DA_CALL)
                .endControlFlow()

                .addStatement(RETURN_RESPONSE)
                .build();

        MethodSpec call = MethodSpec.methodBuilder(api.getMethodName())
                .addParameter(api.getRq(), "rq")
                .addModifiers(Modifier.PUBLIC)
                .returns(api.getRs())
                .addCode(callBody)
                .addException(Exception.class)
                .build();

        TypeSpec da = TypeSpec.classBuilder(api.getDaClass().simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(call)
                .addField(CommonUtil.getLogger(api.getDaClass().simpleName()))
                .addField(serviceName,beanName)
                .build();

        JavaFile javaFile = JavaFile.builder(api.getDaClass().packageName(), da)
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

        //TODO Переписать логгер
        CodeBlock getAllBody = CodeBlock.builder()
                .addStatement("$T request = acceptor.getSbrfSmbAccountingRequest()", sbrfSmbAccountingRequest)
                .addStatement(INITIALIZE_REQUEST, legalBalInqRqType)
                .addStatement(INITIALIZE_RESPONSE, legalBalInqRsType)
                .addStatement(INITIALIZE_SUCCESS_FLAG)
                .beginControlFlow(TRY)
                    .addStatement("rq = GetLegalAccountBalanceRqMapper.map(request)")
                    .addStatement("LOGGER.info(LOGGER_MESSAGE_INTEGRATION_ADAPTER_CALL.getMessage(BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE))")
                    .addStatement("rs = srvGetLegalAccountBalanceSyncClient.sendRequest(new RequestDescriptionImpl<>(rq)).getMessage()")
                    .addStatement("LOGGER.info(LOGGER_MESSAGE_INTEGRATION_ADAPTER_RETURN.getMessage(BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE))")
                    .addStatement("GetLegalAccountBalanceRsMapper.map(rs, request)")
                    .addStatement(CHANGE_SUCCESS_FLAG)
                .nextControlFlow(CATCH, ExecutionException.class)
                .addStatement(EXCEPTION_THROW)
                .nextControlFlow(CATCH, ClassName.get("ru.sbrf.ufs.integration.module.exception","IntegrationException"))
                    .addStatement("LOGGER.error(LOGGER_MESSAGE_INTEGRATION_ADAPTER_ERROR.getMessage(BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE), e)")
                    .addStatement("throw ExecutionExceptionMapper.getExecutionException(e)")
                .nextControlFlow(CATCH, Exception.class)
                    .addStatement("throw new ExecutionException(INTERNAL_ERROR, e)")
                .nextControlFlow(FINALLY)
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
                .addField(CommonUtil.getLogger(className))
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
