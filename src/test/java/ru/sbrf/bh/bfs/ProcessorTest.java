package ru.sbrf.bh.bfs;

import freemarker.template.TemplateException;
import org.testng.annotations.Test;
import ru.sbrf.bh.bfs.model.Configuration;

import java.io.IOException;

public class ProcessorTest {

    //TODO: нельзя тесты использовать для генерации - тут надо ее проверять
    @Test
    public void testProcess() throws IOException, TemplateException {
        Configuration configuration = ConfigurationParser.parse("/smbaccounting.bfs");
        Processor p = new Processor(configuration);

        p.initTemplates("src\\main\\resources\\templates\\7.0");
        p.prepareTargetDir();
        p.createPom();
        p.createApiAll();

    }

    @Test
    public void newProcessorTestV6() throws IOException, TemplateException {
        Configuration configuration = ConfigurationParser.parse("/smbaccounting.bfs");
        ru.sbrf.bh.bfs.generator.Processor processor = new ru.sbrf.bh.bfs.generator.Processor();

        processor.generateBfs(configuration,"src\\main\\resources\\templates\\6.9", PoetFactory.getPoetList());
    }

    @Test
    public void newProcessorTestV7() throws IOException, TemplateException {
        Configuration configuration = ConfigurationParser.parse("/smbaccounting.bfs");
        ru.sbrf.bh.bfs.generator.Processor processor = new ru.sbrf.bh.bfs.generator.Processor();

        processor.generateBfs(configuration,"src\\main\\resources\\templates\\7.0", PoetFactory.getPoetList());
    }
}