package ru.sbrf.bh.bfs;

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

public class Processor {
    private HashMap<String, String> properties;
    private HashMap<String, String> build;
    private List<Adapter> adapters;
    private TemplateGenerator templateGenerator;

    public Processor() {
        properties = new HashMap<String, String>();
        build = new HashMap<String, String>();
        adapters = new ArrayList<Adapter>();
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
        }
        properties = p;
        build = b;
        adapters = ad;
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

    public void initTemplates(String templatePath) throws IOException {
        templateGenerator = new TemplateGenerator(templatePath);

    }



}
