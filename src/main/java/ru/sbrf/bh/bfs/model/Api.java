package ru.sbrf.bh.bfs.model;

import com.squareup.javapoet.ClassName;
import ru.sbrf.bh.bfs.ufs.parser.ReflectionParser;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Api implements Supplier<Boolean>, ServiceInitializer<Api>{

    private static final String DEFAULT_METHOD_NAME = "call";

    private ClassName fgClass;
    private ClassName daClass;
    private ClassName rq;
    private ClassName rs;
    private ClassName service;
    private String methodName;
    private String name;
    private String monitoringSuccessEventName =  "BFS_SUCCESS_EVENT";
    private String monitoringFailEventName = "BFS_FAIL_EVENT";
    private String monitoringMetricName = "BFS_METRIC";
    private ClassName monitoringService;
    private ClassName auditService;

    public static final Map apiMap;
    static {
        Map<String, Method> tmpApiMap = new HashMap<>();
        tmpApiMap.put("fgClass", ReflectionParser.getMethod(Api.class,"setFgClass",String.class));
        tmpApiMap.put("daClass",ReflectionParser.getMethod(Api.class,"setDaClass",String.class));
        tmpApiMap.put("rq",ReflectionParser.getMethod(Api.class,"setRq",String.class));
        tmpApiMap.put("rs",ReflectionParser.getMethod(Api.class,"setRs",String.class));
        tmpApiMap.put("service",ReflectionParser.getMethod(Api.class,"setService",String.class));
        tmpApiMap.put("methodName",ReflectionParser.getMethod(Api.class,"setMethodName",String.class));
        tmpApiMap.put("name",ReflectionParser.getMethod(Api.class,"setName",String.class));
        tmpApiMap.put("monitoringFailEventName",ReflectionParser.getMethod(Api.class,"setMonitoringFailEventName",String.class));
        tmpApiMap.put("monitoringSuccessEventName",ReflectionParser.getMethod(Api.class,"setMonitoringSuccessEventName",String.class));
        tmpApiMap.put("monitoringMetricName",ReflectionParser.getMethod(Api.class,"setMonitoringMetricName",String.class));

        apiMap= Collections.unmodifiableMap(tmpApiMap);
    }

    public ClassName getFgClass() {
        return fgClass;
    }

    ClassName toCN(String name) {
        return name==null?null:ClassName.bestGuess(name);
    }

    public Api setFgClass(ClassName fgClass) {
        this.fgClass = fgClass;
        return this;
    }

    public Api setFgClass(String fgClass) {
        this.fgClass = toCN(fgClass);
        return this;
    }

    public ClassName getDaClass() {
        return daClass;
    }

    public Api setDaClass(ClassName daClass) {
        this.daClass = daClass;
        return this;
    }


    public Api setDaClass(String daClass) {
        this.daClass = toCN(daClass);
        return this;
    }

    public ClassName getRq() {
        return rq;
    }

    public Api setRq(ClassName rq) {
        this.rq = rq;
        return this;
    }


    public Api setRq(String rq) {
        this.rq = toCN(rq);
        return this;
    }

    public ClassName getRs() {
        return rs;
    }

    public Api setRs(ClassName rs) {
        this.rs = rs;
        return this;
    }

    public Api setRs(String rs) {
        this.rs = toCN(rs);
        return this;
    }

    public ClassName getService() {
        return service;
    }

    public Api setService(ClassName service) {
        this.service = service;
        return this;
    }

    public Api setService(String service) {
        this.service = toCN(service);
        return this;
    }

    public String getMethodName() {
        return (methodName==null? DEFAULT_METHOD_NAME :methodName);
    }

    public Api setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public String getName() {
        return name;
    }

    public Api setName(String name) {
        this.name = name;
        return this;
    }

    public Api setMonitoringSuccessEventName(String monitoringSuccessEventName){
        this.monitoringSuccessEventName = monitoringSuccessEventName;
        return this;
    }

    public String getMonitoringSuccessEventName(){
        return monitoringSuccessEventName;
    }

    public Api setMonitoringMetricName(String monitoringMetricName){
        this.monitoringMetricName = monitoringMetricName;
        return this;
    }

    public String getMonitoringMetricName(){
        return monitoringMetricName;
    }

    public Api setMonitoringFailEventName(String monitoringFailEventName){
        this.monitoringFailEventName = monitoringFailEventName;
        return this;
    }

    public String getMonitoringFailEventName(){
        return monitoringFailEventName;
    }


    public ClassName getMonitoringService() {
        return monitoringService;
    }

    public Api setMonitoringService(String monitoringService) {
        this.monitoringService = toCN(monitoringService);
        return this;
    }

    public ClassName getAuditService() {
        return auditService;
    }

    public Api setAuditService(ClassName auditService) {
        this.auditService = auditService;
        return this;
    }

    public Api setAuditService(String auditService) {
        this.auditService = toCN(auditService);
        return this;
    }

    @Override
    public String toString() {
        return "Api{" +
                "methodName='" + methodName + '\'' +
                ", name='" + name + '\'' +
                ", monitoringSuccessEventName='" + monitoringSuccessEventName + '\'' +
                ", monitoringFailEventName='" + monitoringFailEventName + '\'' +
                ", monitoringMetricName='" + monitoringMetricName + '\'' +
                ", monitoringService='" + monitoringService + '\'' +
                '}';
    }


    @Override
    public Boolean get() {
        return this.getFgClass() != null
                && this.getDaClass() != null
                && this.getRq() != null
                && this.getRs() != null;
    }
}
