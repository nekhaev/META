package ru.sbrf.bh.bfs.generator.enums;

/**
 * Created by sbt-barsukov-sv on 13.06.2018.
 */
public enum ApiFields{

    FG_CLASS ("fgClass"),
    DA_CLASS  ("daClass"),
    RQ ("rq"),
    RS  ("rs"),
    SERVICE ("service"),
    METHOD_NAME ("methodName"),
    NAME ("name"),
    MONITORING_FAIL_EVENT_NAME("monitoringFailEventName"),
    MONITORING_SUCCESS_EVENT_NAME( "monitoringSuccessEventName"),
    MONITORING_METRIC_NAME ("monitoringMetricName");

    private String field;


    ApiFields(String field) {

            this.field = field;

    }

    public String getField() {
        return field;
    }

}
