package ru.sbrf.bh.bfs.generator.enums;

import ru.sbrf.bh.bfs.model.Api;

import java.lang.reflect.Method;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public enum ApiFields {

    FG_CLASS ("fgClass", "setFgClass"),
    DA_CLASS  ("daClass", "setDaClass"),
    RQ ("rq", "setRq"),
    RS  ("rs", "setRs"),
    SERVICE ("service", "setService"),
    METHOD_NAME ("methodName", "setMethodName"),
    NAME ("name", "setName"),
    MONITORING_FAIL_EVENT_NAME("monitoringFailEventName", "setMonitoringFailEventName"),
    MONITORING_SUCCESS_EVENT_NAME( "monitoringSuccessEventName", "setMonitoringSuccessEventName"),
    MONITORING_METRIC_NAME ("monitoringMetricName","setMonitoringMetricName");

    private String field;
    private Method method;

    ApiFields(String field, String methodName) {
        try {
            this.field = field;
            this.method = Api.class.getMethod(methodName, String.class);
        } catch (Exception ex) {
            this.method=null;
        }
    }

    public String getField() {
        return field;
    }

    public Method getMethod() {
        return method;
    }

}
