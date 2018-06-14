package ru.sbrf.bh.bfs.ufs.parser;

import ru.sbrf.bh.bfs.generator.enums.ParamFields;
import ru.sbrf.bh.bfs.generator.literals.Services;
import ru.sbrf.bh.bfs.generator.parser.Parser;
import ru.sbrf.bh.bfs.grammar.BfsParser;
import ru.sbrf.bh.bfs.model.Adapter;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.model.Configuration;

import java.util.*;

/**
 * Парсер конфигурационного файла *.bfs
 * @author sbt-kotsyuruba-di
 * @since 28.05.2018
 */
public class ConfigurationParser extends Parser<BfsParser,Configuration>{

    private static final Map<ParamFields,Parser> parserMap;
    static {
        Map <ParamFields,Parser> tmpMap = new HashMap<>();
        ToHashMapParser toHashMapParser = new ToHashMapParser();
        tmpMap.put(ParamFields.PROPERTIES,toHashMapParser);
        tmpMap.put(ParamFields.SERVICES,toHashMapParser);
        tmpMap.put(ParamFields.BUILD,toHashMapParser);
        tmpMap.put(ParamFields.ADAPTERS,new MavenDependencyParser());
        tmpMap.put(ParamFields.API,new ApiParserHelper());
        parserMap = Collections.unmodifiableMap(tmpMap);
    }


    public void parse(String configPath,BfsParser source, Configuration target){

        Map<String, String> p = new HashMap<>() ;
        Map<String, String> b = new HashMap<>() ;
        Map<String, String> s = new HashMap<>();
        List<Adapter> ad = new ArrayList<>();
        List<Api> apis = new ArrayList<>();

        for (BfsParser.GroupContext gc : (source.r()).group()) {

            parserMap.get(ParamFields.PROPERTIES).parse(ParamFields.PROPERTIES.getField(),gc,p);
            parserMap.get(ParamFields.SERVICES).parse(ParamFields.SERVICES.getField(),gc,s);
            parserMap.get(ParamFields.BUILD).parse(ParamFields.BUILD.getField(),gc,b);
            parserMap.get(ParamFields.ADAPTERS).parse(ParamFields.ADAPTERS.getField(),gc,ad);
            ((ApiParserHelper)parserMap.get(ParamFields.API))
                    .setMonitoringServiceName(s.get(ParamFields.MONITORING_SERVICE.getField())+"."+ Services.MONITORING)
                    .parse(ParamFields.API.getField(),gc,apis);
        }
        target.setProperties(p);
        target.setBuild(b);
        target.setAdapters(ad);
        target.setApis(apis);
        target.setServices(s);
    }

}
