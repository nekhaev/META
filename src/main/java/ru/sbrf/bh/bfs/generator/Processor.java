package ru.sbrf.bh.bfs.generator;

import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.model.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public class Processor {


    private static final Logger LOGGER = Logger.getLogger("logger");


    public HashMap<String,Object> getModel(Configuration configuration) {
        HashMap<String,Object> model = new HashMap<String, Object>(configuration.getProperties());
        model.put("adapters", configuration.getAdapters());
        return model;
    }

    public File basePath(Configuration configuration) {
        return new File(configuration.getBuild().get("outputDir"));
    }
    public File javaPath(Configuration configuration) {
        return new File(basePath(configuration), "src\\main\\java");
    }

    public void prepareTargetDir(Configuration configuration) throws IOException {
        FileUtils.forceMkdir(basePath(configuration));
        FileUtils.cleanDirectory(basePath(configuration));
        FileUtils.forceMkdir(javaPath(configuration));
    }


    public void createPom(TemplateGenerator templateGenerator, Configuration configuration) throws IOException, TemplateException {

        File f = new File(basePath(configuration), "pom.xml");
        if(!f.createNewFile()) {
            // file exists
        }

        Writer out = new FileWriter(f);
        templateGenerator.makeFile("pom.ftl", getModel(configuration),out);
        out.close();
    }


    //Может пригодиться, когда классов будет больше
    public void createApi(Api api, List<TypePoet> poets, Configuration configuration)throws IOException {
        for (TypePoet poet:poets ) {
            poet.makeSimple(api,javaPath(configuration));
            LOGGER.info(poet.getClass().toString() + " ready");
        }
    }

    public void generateBfs(Configuration config,String path, List<TypePoet> poets)throws IOException, TemplateException{
        prepareTargetDir(config);
        createPom(new TemplateGenerator(path),config);
        for(Api api: config.getApis()) {
            createApi(api,poets, config);
        }
    }



}
