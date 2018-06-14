package ru.sbrf.bh.bfs.ufs.parser;

import ru.sbrf.bh.bfs.generator.enums.ApiFields;
import ru.sbrf.bh.bfs.generator.parser.Parser;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.grammar.BfsParser.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Created by sbt-barsukov-sv on 14.06.2018.
 */
public class ApiParserHelper extends Parser<GroupContext,List<Api>> {

    private String monitoringServiceName;

    public ApiParserHelper(String monitoringServiceName) {
        this.monitoringServiceName = monitoringServiceName;
    }

    public ApiParserHelper() {
    }

    public void parse(String key, GroupContext gc, List<Api> apis){
        if (key.equals(gc.ID().getText())) {

            for (GroupContext igc : gc.group()) {
                final Api api = new Api().setName(igc.ID().getText()).setMonitoringService(monitoringServiceName);

                for (PropertyContext pc : igc.property()) {
                    final String text = trm(pc.STRING().getText());
                    final String name = pc.ID().getText();

                    Arrays.stream(ApiFields.values()).forEach(field -> {
                                            if (field.getField().equals(name) && field.getMethod() != null && text!=null)
                                                try {
                                                    field.getMethod().invoke(api,text);
                                                } catch (Exception ex) {
                                                    LOGGER.warning("Can not update "+field.getField());
                                                }
                                            });

                }
                if (validateApi(api))
                    apis.add(api);
            }
        }
    }



    public static boolean validateApi(Api api) {
        return api.getFgClass() != null
                && api.getDaClass() != null
                && api.getRq() != null
                && api.getRs() != null;
    }

    public ApiParserHelper setMonitoringServiceName(String monitoringServiceName) {
        this.monitoringServiceName = monitoringServiceName;
        return this;
    }
}

