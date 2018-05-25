package ru.sbrf.bh.bfs;

import com.squareup.javapoet.ClassName;
import freemarker.template.TemplateException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.io.FileUtils;
import ru.sbrf.bh.bfs.grammar.bfsParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class Processor {

    Logger logger = Logger.getLogger("logger");
    private HashMap<String, String> properties;
    private HashMap<String, String> build;
    private List<Adapter> adapters;
    private TemplateGenerator templateGenerator;
    private List<Api> apis;

    public Processor() {
        properties = new HashMap<String, String>();
        build = new HashMap<String, String>();
        adapters = new ArrayList<Adapter>();
        apis = new ArrayList<Api>();
    }

    public static void main(String[] args) throws Exception {
        Processor p = new Processor();
        p.init("/smbaccounting2.bfs");
    }

    public void init(String config) throws IOException {
        System.out.println("Hello");
        HelloLexer lexer = new HelloLexer(
                CharStreams.fromStream(
                        this.getClass().getResourceAsStream(config)
                ));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HelloParser parser = new HelloParser(tokens);
        ParseTree tree = parser.r();
//        ParseTreeWalker walker = new ParseTreeWalker();
//        walker.walk(new HelloWalker(), tree);

        HashMap<String, String> p = new HashMap<String, String>();
        HashMap<String, String> b = new HashMap<String, String>();
        List<Adapter> ad = new ArrayList<Adapter>();
        List<Api> apis = new ArrayList<Api>();

        for (bfsParser.GroupContext gc : ((bfsParser.RContext) tree).group()) {
            if ("properties".equals(gc.ID().getText())) {
                for (bfsParser.PropertyContext pc : gc.property()) {
                    p.put(pc.ID().getText(), trm(pc.STRING().getText()));
                }
            }
            if ("build".equals(gc.ID().getText())) {
                for (bfsParser.PropertyContext pc : gc.property()) {
                    b.put(pc.ID().getText(), trm(pc.STRING().getText()));
                }
            }
            if ("adapters".equals(gc.ID().getText())) {
                for (bfsParser.GroupContext igc : gc.group()) {
                    String groupId = null, artifactId = null, version = null;
                    for (bfsParser.PropertyContext pc : igc.property()) {
                        String text = trm(pc.STRING().getText());
                        String name = pc.ID().getText();

                        if ("groupId".equals(name)) groupId = text;
                        if ("artifactId".equals(name)) artifactId = text;
                        if ("version".equals(name)) version = text;
                    }
                    if (groupId != null && artifactId != null && version != null) {
                        ad.add(new Adapter(groupId, artifactId, version));
                    }
                }
            }
            if ("api".equals(gc.ID().getText())) {
                for (bfsParser.GroupContext igc : gc.group()) {
                    String fgClass = null, daClass = null, rq = null, rs = null, service = null, methodName = null
                            ,apiName = igc.ID().getText();
                    for (bfsParser.PropertyContext pc : igc.property()) {
                        String text = trm(pc.STRING().getText());
                        String name = pc.ID().getText();

                        if ("fgClass".equals(name)) fgClass = text;
                        if ("daClass".equals(name)) daClass = text;
                        if ("rq".equals(name)) rq = text;
                        if ("rs".equals(name)) rs = text;
                        if ("service".equals(name)) service = text;
                        if ("methodName".equals(name)) methodName = text;
                        if ("name".equals(name)) name = text;
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
        properties = p;
        build = b;
        adapters = ad;
        this.apis = apis;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public HashMap<String, String> getBuild() {
        return build;
    }

    public String trm(String s) {
        return s.substring(1, s.length() - 1);
    }

    public List<Adapter> getAdapters() {
        return adapters;
    }

    public HashMap<String,Object> getModel() {
        HashMap<String,Object> model = new HashMap<String, Object>(properties);
        model.put("adapters",getAdapters());
        return model;
    }

    public File basePath() {
        return new File(build.get("outputDir"));
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
        templateGenerator.makeFile("pom.ftl",getModel(),out);
        out.close();
    }

    public void createApi(Api api) throws IOException {

        String methodName = api.getMethodName();

        DaPoet daPoet = new DaPoet(javaPath());
        daPoet.makeSimple(api);
        logger.info(api.getDaClass().toString());

        FgPoet poet = new FgPoet(javaPath());
        poet.makeSimple(api);
        logger.info(api.getFgClass().toString());

    }

    public void createApiAll() throws IOException {
        for(Api api: apis) {
            createApi(api);
        }
    }

    public void initTemplates(String templatePath) throws IOException {
        templateGenerator = new TemplateGenerator(templatePath);

    }



}
