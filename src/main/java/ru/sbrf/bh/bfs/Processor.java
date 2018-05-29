package ru.sbrf.bh.bfs;

import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import ru.sbrf.bh.bfs.generator.TypePoet;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.model.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.logging.Logger;

public class Processor {

    private static final Logger LOGGER = Logger.getLogger("logger");
    private Configuration configuration;
    private TemplateGenerator templateGenerator;

    public Processor(Configuration configuration) {
        this.configuration = configuration;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = ConfigurationParser.parse("/smbaccounting.bfs");
//        Processor p = new Processor();
//        p.init();
    }

    public HashMap<String,Object> getModel() {
        HashMap<String,Object> model = new HashMap<String, Object>(configuration.getProperties());
        model.put("adapters", configuration.getAdapters());
        return model;
    }

    public File basePath() {
        return new File(configuration.getBuild().get("outputDir"));
    }
    public File javaPath() {
        return new File(basePath(), "src\\main\\java");
    }

    public void prepareTargetDir() throws IOException {
        FileUtils.forceMkdir(basePath());
        FileUtils.cleanDirectory(basePath());
        FileUtils.forceMkdir(javaPath());
    }


    public void createPom() throws IOException, TemplateException {

        File f = new File(basePath(), "pom.xml");
        if(!f.createNewFile()) {
            // file exists
        }

        Writer out = new FileWriter(f);
        templateGenerator.makeFile("pom.ftl", getModel(),out);
        out.close();
    }

    public void createApi(Api api) throws IOException {

        String methodName = api.getMethodName();

        DaPoet daPoet = new DaPoet();
        daPoet.makeSimple(api,javaPath());
        LOGGER.info(api.getDaClass().toString());

        FgPoet poet = new FgPoet();
        poet.makeSimple(api,javaPath());
        LOGGER.info(api.getFgClass().toString());

    }

    public void createApiAll() throws IOException {
        for(Api api: configuration.getApis()) {
            createApi(api);
        }
    }

    public void initTemplates(String templatePath) throws IOException {
        templateGenerator = new TemplateGenerator(templatePath);

    }

    //Может пригодиться, когда классов будет больше
    public void createApi(Api api, TypePoet... poets)throws IOException {
        for (TypePoet poet:poets ) {
            poet.makeSimple(api,javaPath());
            LOGGER.info(poet.getClass().toString() + "ready");
        }
    }



}
