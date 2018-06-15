package ru.sbrf.bh.bfs.ufs.parser;

import ru.sbrf.bh.bfs.generator.parser.Parser;
import ru.sbrf.bh.bfs.grammar.BfsParser.*;
import ru.sbrf.bh.bfs.model.ServiceInitializer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


/**
 * Created by sbt-barsukov-sv on 14.06.2018.
 */
public class ReflectionParser<T extends Supplier<Boolean>&ServiceInitializer<T>> extends Parser<GroupContext,List<T>> {

    private String monitoringServiceName;
    private Class<T> clazz;
    private Map<String,Method> mapping;

    public ReflectionParser(Class<T> clazz, Map mapping ) {
        this.clazz = clazz;
        this.mapping = mapping;
    }

    public void parse(String key, GroupContext gc, List<T> list){
        if (key.equals(gc.ID().getText())) {

            for (GroupContext igc : gc.group()) {
                try {
                    final T instance = clazz.newInstance().setName(igc.ID().getText()).setMonitoringService(monitoringServiceName);

                    for (PropertyContext pc : igc.property()) {
                        final String text = trm(pc.STRING().getText());
                        final String name = pc.ID().getText();

                        try {
                            mapping.get(name).invoke(instance, text);
                        } catch (Exception ex) {
                                    LOGGER.warning("Can not update " + name);
                        }

                    }
                    if (instance.get())
                        list.add(instance);
                } catch (Exception e){
                    LOGGER.warning("Can create instance " + clazz);
                }
            }
        }
    }


    public ReflectionParser setMonitoringServiceName(String monitoringServiceName) {
        this.monitoringServiceName = monitoringServiceName;
        return this;
    }

    public static Method getMethod(Class clazz, String name){
        try {
            return clazz.getMethod(name);
        } catch (Exception ex){
            return null;
        }

    }

}

