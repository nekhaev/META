package ru.sbrf.bh.bfs.ufs.parser;

import java.util.Map;

import ru.sbrf.bh.bfs.generator.parser.Parser;
import ru.sbrf.bh.bfs.grammar.BfsParser.*;

/**
 * Created by sbt-barsukov-sv on 14.06.2018.
 */
public class ToHashMapParser extends Parser<GroupContext,Map<String,String>> {

    public void   parse(String key, GroupContext source, Map<String,String> target){

        if (key.equals(source.ID().getText())) {
            for (PropertyContext pc : source.property()) {
                target.put(pc.ID().getText(), trm(pc.STRING().getText()));
            }
        }
    }

}
