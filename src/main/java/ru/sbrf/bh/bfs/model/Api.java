package ru.sbrf.bh.bfs.model;

import com.squareup.javapoet.ClassName;

public class Api {

    private static final String DEFAULT_METHOD_NAME = "call";

    private ClassName fgClass;
    private ClassName daClass;
    private ClassName rq;
    private ClassName rs;
    private ClassName service;
    private String methodName;
    private String name;

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
}
