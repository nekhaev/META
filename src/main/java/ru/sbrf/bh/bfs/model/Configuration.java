package ru.sbrf.bh.bfs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Конфигурация для генерации БФС
 * @author sbt-kotsyuruba-di
 * @since 28.05.2018
 */
public class Configuration {

    private Map<String, String> properties;
    private Map<String, String> build;
    private List<Adapter> adapters;
    private List<Api> apis;
    private Map<String, String> services;

    public Configuration() {
        properties = new HashMap<>();
        build = new HashMap<>();
        adapters = new ArrayList<>();
        apis = new ArrayList<>();
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getBuild() {
        return build;
    }

    public void setBuild(Map<String, String> build) {
        this.build = build;
    }

    public List<Adapter> getAdapters() {
        return adapters;
    }

    public void setAdapters(List<Adapter> adapters) {
        this.adapters = adapters;
    }

    public List<Api> getApis() {
        return apis;
    }

    public void setApis(List<Api> apis) {
        this.apis = apis;
    }

    public Map<String, String> getServices() {
        return services;
    }

    public void setServices(Map<String, String> services) {
        this.services = services;
    }
}
