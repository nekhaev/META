package ru.sbrf.bh.bfs.ufs.parser;

import ru.sbrf.bh.bfs.generator.enums.ApiFields;
import ru.sbrf.bh.bfs.generator.enums.ArtifactFields;
import ru.sbrf.bh.bfs.generator.enums.ParamFields;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import ru.sbrf.bh.bfs.generator.literals.Services;
import ru.sbrf.bh.bfs.grammar.BfsLexer;
import ru.sbrf.bh.bfs.grammar.BfsParser;
import ru.sbrf.bh.bfs.model.Adapter;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.model.Configuration;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Парсер конфигурационного файла *.bfs
 * @author sbt-kotsyuruba-di
 * @since 28.05.2018
 */
public class ConfigurationParser {

    private static final Logger LOGGER = Logger.getLogger("logger");
    public static Configuration parse(String configPath) throws IOException {
        BfsLexer lexer = new BfsLexer(
                CharStreams.fromStream(
                        ConfigurationParser.class.getResourceAsStream(configPath)
                ));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BfsParser parser = new BfsParser(tokens);
        ParseTree tree = parser.r();

        HashMap<String, String> p = new HashMap<>();
        HashMap<String, String> b = new HashMap<>();
        HashMap<String, String> s = new HashMap<>();
        List<Adapter> ad = new ArrayList<>();
        List<Api> apis = new ArrayList<>();

        for (BfsParser.GroupContext gc : ((BfsParser.RContext) tree).group()) {
            if (ParamFields.PROPERTIES.getField().equals(gc.ID().getText())) {
                for (BfsParser.PropertyContext pc : gc.property()) {
                    p.put(pc.ID().getText(), trm(pc.STRING().getText()));
                }
            }
            if (ParamFields.SERVICES.getField().equals(gc.ID().getText())) {
                for (BfsParser.PropertyContext pc : gc.property()) {
                    s.put(pc.ID().getText(), trm(pc.STRING().getText()));
                }
            }
            if (ParamFields.BUILD.getField().equalsIgnoreCase(gc.ID().getText())) {
                for (BfsParser.PropertyContext pc : gc.property()) {
                    b.put(pc.ID().getText(), trm(pc.STRING().getText()));
                }
            }
            if (ParamFields.ADAPTERS.getField().equals(gc.ID().getText())) {
                for (BfsParser.GroupContext igc : gc.group()) {
                    String groupId = null, artifactId = null, version = null;
                    for (BfsParser.PropertyContext pc : igc.property()) {
                        String text = trm(pc.STRING().getText());
                        String name = pc.ID().getText();

                        if (ArtifactFields.GROUP_ID.getField().equals(name)) groupId = text;
                        if (ArtifactFields.ARTIFACT_ID.getField().equals(name)) artifactId = text;
                        if (ArtifactFields.VERSION.getField().equals(name)) version = text;
                    }
                    if (groupId != null && artifactId != null && version != null) {
                        ad.add(new Adapter(groupId, artifactId, version));
                    }
                }
            }
            if (ParamFields.API.getField().equals(gc.ID().getText())) {

                for (BfsParser.GroupContext igc : gc.group()) {
                    String apiName = igc.ID().getText();
                    Map<ApiFields,String> fields = new HashMap<>();
                    for (BfsParser.PropertyContext pc : igc.property()) {
                        final String text = trm(pc.STRING().getText());
                        final String name = pc.ID().getText();


                        Arrays.stream(ApiFields.values()).forEach(field -> {if (field.getField().equals(name)) fields.put(field,text);});


                    }
                    if (fields.get(ApiFields.FG_CLASS) != null
                            && fields.get(ApiFields.DA_CLASS) != null
                            && fields.get(ApiFields.RQ) != null
                            && fields.get(ApiFields.RS) != null
                            ) {
                        final Api api = new Api().setName(apiName).setMonitoringService(s.get(ParamFields.MONITORING_SERVICE.getField())+"."+ Services.MONITORING);

                        Arrays.stream(ApiFields.values()).forEach(apiField -> {
                            try {
                                if (apiField.getMethod() != null && fields.get(apiField) != null)
                                    apiField.getMethod().invoke(api,fields.get(apiField));
                            } catch (Exception ex) {
                                LOGGER.warning("Can not update "+apiField.getField());
                            }
                        });

                        apis.add(api);
                    }
                }
            }
        }
        Configuration configuration = new Configuration();
        configuration.setProperties(p);
        configuration.setBuild(b);
        configuration.setAdapters(ad);
        configuration.setApis(apis);
        configuration.setServices(s);
        return configuration;
    }

    private static String trm(String s) {
        return s.substring(1, s.length() - 1);
    }

}
