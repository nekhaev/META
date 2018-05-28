package ru.sbrf.bh.bfs;

import freemarker.template.TemplateException;
import org.testng.annotations.Test;

import java.io.IOException;

public class ProcessorTest {

    @Test
    public void testProcess() throws IOException, TemplateException {
        Processor p = new Processor();

        p.initTemplates("src\\main\\resources\\templates\\7.0");
        p.init("/smbaccounting3.bfs");

        p.prepareTargetDir();
        p.createPom();
        p.createApiAll();

    }
}