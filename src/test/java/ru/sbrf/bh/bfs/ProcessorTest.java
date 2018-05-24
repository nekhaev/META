package ru.sbrf.bh.bfs;

import com.squareup.javapoet.ClassName;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import java.io.*;

public class ProcessorTest {

    @Test
    public void testProcess() throws IOException, TemplateException {
        Processor p = new Processor();

        p.initTemplates("C:\\Users\\ndn\\IdeaProjects\\META\\src\\main\\resources\\templates\\7.0");
        p.init("/smbaccounting3.bfs");

        p.prepareTargetDir();
        p.createPom();
        p.createApiAll();

    }
}