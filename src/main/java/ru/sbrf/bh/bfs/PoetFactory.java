package ru.sbrf.bh.bfs;

import ru.sbrf.bh.bfs.generator.TypePoet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public class PoetFactory {

    private static class PoetListRepo {
        private final static List<TypePoet> poetList = Collections.unmodifiableList(new ArrayList<TypePoet>(){{
                                                                add(new DaPoet());
                                                                add(new FgPoet());
                                                            }});
    }

    public static List<TypePoet> getPoetList() {
        return PoetListRepo.poetList;
    }

}
