package ru.sbrf.bh.bfs;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;

public class TemplateGenerator {
    private String templatePath;
    private Configuration cfg;

    public TemplateGenerator(String pathname) throws IOException {
        templatePath = pathname;
        cfg = init(templatePath);
    }

    Configuration init(String pathname) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setDirectoryForTemplateLoading(new File(pathname));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setInterpolationSyntax(Configuration.SQUARE_BRACKET_INTERPOLATION_SYNTAX);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);

        return cfg;
    }

    void makeFile(String templateName, HashMap model, Writer out) throws IOException, TemplateException {
        Template temp = cfg.getTemplate(templateName);

        temp.process(model, out);

    }
}


