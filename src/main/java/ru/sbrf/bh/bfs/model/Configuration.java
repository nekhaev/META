package ru.sbrf.bh.bfs.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Конфигурация для генерации БФС
 * @author sbt-kotsyuruba-di
 * @since 28.05.2018
 */
public class Configuration {

    private HashMap<String, String> properties;
    private HashMap<String, String> build;
    private List<Adapter> adapters;
    private List<Api> apis;

    public Configuration() {
        properties = new HashMap<String, String>();
        build = new HashMap<String, String>();
        adapters = new ArrayList<Adapter>();
        apis = new ArrayList<Api>();
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    public HashMap<String, String> getBuild() {
        return build;
    }

    public void setBuild(HashMap<String, String> build) {
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

}
