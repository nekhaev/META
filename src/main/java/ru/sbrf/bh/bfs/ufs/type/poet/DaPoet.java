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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DaPoet extends ApiTypePoet<Api> {

    private final static String BEFORE_DA_CALL = "Before DA call";
    private final static String AFTER_DA_CALL = "After DA call";
    private final static String EXIT_DA_CALL = "Exit DA call";

    private static class CallMethodPoet extends MethodPoet<Api> {
        protected CodeBlock createMethodBlock(Api api) {
            CodeBlock.Builder builder =  CodeBlock.builder()
                    .addStatement(Statement.INITIALIZE_RESPONSE, api.getRs())
                    .addStatement(Statement.INITIALIZE_SUCCESS_FLAG);

            if(api.getMonitoringService() != null)
                builder.addStatement(Statement.MONITORING_SERVICE_START, api.getMonitoringSuccessEventName());

            builder.beginControlFlow(ControlFlow.TRY)
                    .addStatement(Statement.LOGGER_INFO_LEVEL, BEFORE_DA_CALL)
                    .addStatement(Statement.DA_SEND_REQUEST
                            , BeanNames.INTEGRATION_BEAN
                            , ClassName.get("ru.sbrf.ufs.integration.module","RequestDescriptionImpl"));

            if(api.getMonitoringService() != null)
                builder.addStatement(Statement.MONITORING_SERVICE_STOP,api.getMonitoringSuccessEventName(),api.getMonitoringMetricName());

            builder.addStatement(Statement.LOGGER_INFO_LEVEL, AFTER_DA_CALL)
                    .addStatement(Statement.CHANGE_SUCCESS_FLAG)
                    .nextControlFlow(ControlFlow.CATCH, Exception.class);
            if(api.getMonitoringService() != null)
                builder.addStatement(Statement.MONITORING_SERVICE_STOP,api.getMonitoringFailEventName(),api.getMonitoringMetricName());
            builder.addStatement(Statement.EXCEPTION_THROW)
                    .nextControlFlow(ControlFlow.FINALLY)
                    .addStatement(Statement.LOGGER_INFO_LEVEL, EXIT_DA_CALL)
                    .endControlFlow()

                    .addStatement(Statement.RETURN_RESPONSE);
            return  builder.build();
        }



        @Override
        protected void createDeclaration(Api param, MethodSpec.Builder specBuilder) {
            specBuilder.addParameter(param.getRq(), ApiFields.RQ.getField())
                    .addModifiers(Modifier.PUBLIC)
                    .returns(param.getRs())
                    .addException(Exception.class);
        }
    }

    protected String getPackageName(Api api) {
        return api.getDaClass().packageName();
    }

    protected TypeSpec createType(Api api) {

         /*
        отличия разных версий адаптеров
         */

        TypeName serviceName = api.getService() != null ? api.getService() :
                ParameterizedTypeName.get(
                        ClassName.get("ru.sbrf.ufs.integration.module.api.call", "SyncCallClient")
                        ,api.getRq()
                        ,api.getRs());

        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(api.getDaClass().simpleName())
                .addModifiers(Modifier.PUBLIC)
                .addMethod((new CallMethodPoet()).createMethod(api, api.getMethodName()))
                .addField(CommonUtil.getLogger(api.getDaClass().simpleName()));
        if (api.getMonitoringService() != null)
            typeSpec.addField(CommonUtil.beanField(api.getMonitoringService(),BeanNames.MONITORING_BEAN));
        if (api.getAuditService() != null)
            typeSpec.addField(CommonUtil.beanField(api.getAuditService(),BeanNames.AUDIT_BEAN));
        typeSpec.addField(CommonUtil.beanField(serviceName,BeanNames.INTEGRATION_BEAN));
        return typeSpec.build();
    }
//TODO
    @Deprecated
    public void run(String className, File outputDir) throws IOException {

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
                .addStatement(Statement.INITIALIZE_REQUEST, legalBalInqRqType)
                .addStatement(Statement.INITIALIZE_RESPONSE, legalBalInqRsType)
                .addStatement(Statement.INITIALIZE_SUCCESS_FLAG)
                .beginControlFlow(ControlFlow.TRY)
                    .addStatement("rq = GetLegalAccountBalanceRqMapper.map(request)")
                    .addStatement("LOGGER.info(LOGGER_MESSAGE_INTEGRATION_ADAPTER_CALL.getMessage(BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE))")
                    .addStatement("rs = srvGetLegalAccountBalanceSyncClient.sendRequest(new RequestDescriptionImpl<>(rq)).getMessage()")
                    .addStatement("LOGGER.info(LOGGER_MESSAGE_INTEGRATION_ADAPTER_RETURN.getMessage(BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE))")
                    .addStatement("GetLegalAccountBalanceRsMapper.map(rs, request)")
                    .addStatement(Statement.CHANGE_SUCCESS_FLAG)
                .nextControlFlow(ControlFlow.CATCH, ExecutionException.class)
                .addStatement(Statement.EXCEPTION_THROW)
                .nextControlFlow(ControlFlow.CATCH, ClassName.get("ru.sbrf.ufs.integration.module.exception","IntegrationException"))
                    .addStatement("LOGGER.error(LOGGER_MESSAGE_INTEGRATION_ADAPTER_ERROR.getMessage(BfsServices.SRV_GET_LEGAL_ACCOUNT_BALANCE), e)")
                    .addStatement("throw ExecutionExceptionMapper.getExecutionException(e)")
                .nextControlFlow(ControlFlow.CATCH, Exception.class)
                    .addStatement("throw new ExecutionException(INTERNAL_ERROR, e)")
                .nextControlFlow(ControlFlow.FINALLY)
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
