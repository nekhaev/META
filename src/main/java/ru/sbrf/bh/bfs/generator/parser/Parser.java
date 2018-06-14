package ru.sbrf.bh.bfs.generator.parser;

import java.util.logging.Logger;

/**
 * Created by sbt-barsukov-sv on 14.06.2018.
 */
public abstract class Parser<T1,T2> {

    protected static final Logger LOGGER = Logger.getLogger("logger");

    public abstract void parse(String key, T1 source, T2 target);

    static protected String trm(String s) {
        return s.substring(1, s.length() - 1);
    }
}
