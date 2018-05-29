package ru.sbrf.bh.bfs;

import ru.sbrf.bh.bfs.generator.StringConsts;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import ru.sbrf.bh.bfs.grammar.BfsLexer;
import ru.sbrf.bh.bfs.grammar.BfsParser;
import ru.sbrf.bh.bfs.model.Adapter;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.model.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Парсер конфигурационного файла *.bfs
 * @author sbt-kotsyuruba-di
 * @since 28.05.2018
 */
public class ConfigurationParser {

    public static Configuration parse(String configPath) throws IOException {
        BfsLexer lexer = new BfsLexer(
                CharStreams.fromStream(
                        ConfigurationParser.class.getResourceAsStream(configPath)
                ));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BfsParser parser = new BfsParser(tokens);
        ParseTree tree = parser.r();

        HashMap<String, String> p = new HashMap<String, String>();
        HashMap<String, String> b = new HashMap<String, String>();
        List<Adapter> ad = new ArrayList<Adapter>();
        List<Api> apis = new ArrayList<Api>();

        for (BfsParser.GroupContext gc : ((BfsParser.RContext) tree).group()) {
            if (StringConsts.PROPERTIES.equals(gc.ID().getText())) {
                for (BfsParser.PropertyContext pc : gc.property()) {
                    p.put(pc.ID().getText(), trm(pc.STRING().getText()));
                }
            }
            if (StringConsts.BUILD.equalsIgnoreCase(gc.ID().getText())) {
                for (BfsParser.PropertyContext pc : gc.property()) {
                    b.put(pc.ID().getText(), trm(pc.STRING().getText()));
                }
            }
            if (StringConsts.ADAPTERS.equals(gc.ID().getText())) {
                for (BfsParser.GroupContext igc : gc.group()) {
                    String groupId = null, artifactId = null, version = null;
                    for (BfsParser.PropertyContext pc : igc.property()) {
                        String text = trm(pc.STRING().getText());
                        String name = pc.ID().getText();

                        if (StringConsts.GROUP_ID.equals(name)) groupId = text;
                        if (StringConsts.ARTIFACT_ID.equals(name)) artifactId = text;
                        if (StringConsts.VERSION.equals(name)) version = text;
                    }
                    if (groupId != null && artifactId != null && version != null) {
                        ad.add(new Adapter(groupId, artifactId, version));
                    }
                }
            }
            if (StringConsts.API.equals(gc.ID().getText())) {
                for (BfsParser.GroupContext igc : gc.group()) {
                    String fgClass = null, daClass = null, rq = null, rs = null, service = null, methodName = null, apiName = igc.ID().getText();
                    for (BfsParser.PropertyContext pc : igc.property()) {
                        String text = trm(pc.STRING().getText());
                        String name = pc.ID().getText();

                        if (StringConsts.FG_CLASS.equals(name)) fgClass = text;
                        if (StringConsts.DA_CLASS.equals(name)) daClass = text;
                        if (StringConsts.RQ.equals(name)) rq = text;
                        if (StringConsts.RS.equals(name)) rs = text;
                        if (StringConsts.SERVICE.equals(name)) service = text;
                        if (StringConsts.METHOD_NAME.equals(name)) methodName = text;
                        if (StringConsts.NAME.equals(name)) name = text;
                    }
                    if (fgClass != null && daClass != null && rq != null
                            && rs != null
                            ) {
                        apis.add(new Api().setFgClass(fgClass)
                                .setDaClass(daClass)
                                .setRq(rq)
                                .setRs(rs)
                                .setService(service)
                                .setMethodName(methodName)
                                .setName(apiName)
                        );
                    }
                }
            }
        }
        Configuration configuration = new Configuration();
        configuration.setProperties(p);
        configuration.setBuild(b);
        configuration.setAdapters(ad);
        configuration.setApis(apis);
        return configuration;
    }

    private static String trm(String s) {
        return s.substring(1, s.length() - 1);
    }

}
