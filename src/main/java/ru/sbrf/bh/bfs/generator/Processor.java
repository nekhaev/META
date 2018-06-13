package ru.sbrf.bh.bfs.generator;

import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.*;
import ru.sbrf.bh.bfs.generator.template.TemplateGenerator;
import ru.sbrf.bh.bfs.generator.type.api.ApiTypePoet;
import ru.sbrf.bh.bfs.generator.type.service.ServiceTypePoet;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.model.Configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by sbt-barsukov-sv on 29.05.2018.
 */
public class Processor {


    private static final Logger LOGGER = Logger.getLogger("logger");


    private HashMap<String,Object> getModel(Configuration configuration) {
        HashMap<String,Object> model = new HashMap<String, Object>(configuration.getProperties());
        model.put("adapters", configuration.getAdapters());
        return model;
    }

    private File basePath(Configuration configuration) {
        return new File(configuration.getBuild().get("outputDir"));
    }
    private File javaPath(Configuration configuration) {
        return new File(basePath(configuration), "src\\main\\java");
    }

    private void prepareTargetDir(Configuration configuration) throws IOException {
        FileUtils.forceMkdir(basePath(configuration));
        FileUtils.cleanDirectory(basePath(configuration));
        FileUtils.forceMkdir(javaPath(configuration));
    }


    private File createPom(TemplateGenerator templateGenerator, Configuration configuration) throws IOException, TemplateException {

        File f = new File(basePath(configuration), "pom.xml");
        if(!f.createNewFile()) {
            // file exists
        }

        Writer out = new FileWriter(f);
        templateGenerator.makeFile("pom.ftl", getModel(configuration),out);
        out.close();
        return f;
    }


    //Может пригодиться, когда классов будет больше
    private void createApi(Api api, List<ApiTypePoet> poets, Configuration configuration){

        poets.forEach(poet -> {poet.makeSimple(api,javaPath(configuration));
                               LOGGER.info(poet.getClass().toString() + " ready");});

    }

    private void jarPackage(File pom) throws IOException,TemplateException{
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(pom);
        request.setGoals(Arrays.asList("clean","package"));

        try {
            (new DefaultInvoker()).execute(request);
        } catch (MavenInvocationException ex) {
            LOGGER.info(ex.getMessage());
        }

    }

    public void generateBfs(Configuration config,String path, List<ApiTypePoet> apiPoets,Map<String,ServiceTypePoet> servicePoets)throws IOException, TemplateException{
        prepareTargetDir(config);
        config.getApis().forEach(api -> {
                                                LOGGER.info(api.toString());
                                                createApi(api, apiPoets, config);
                                            });
        config.getServices().forEach((serviceName,servicePackage) -> {ServiceTypePoet servicePoet = servicePoets.get(serviceName);
                                                        LOGGER.info(serviceName + " "+servicePoet);
                                                        if(servicePoet!=null) {
                                                            servicePoet.makeSimple(servicePackage,javaPath(config));
                                                            LOGGER.info(servicePoet.getClass() +" ready");
                                                        }
                                                        } );
        File pomFile = createPom(new TemplateGenerator(path),config);
        jarPackage(pomFile);
    }



}
