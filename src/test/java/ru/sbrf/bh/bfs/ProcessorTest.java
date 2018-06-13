package ru.sbrf.bh.bfs;

import freemarker.template.TemplateException;
import org.testng.annotations.Test;
import ru.sbrf.bh.bfs.model.Configuration;
import ru.sbrf.bh.bfs.ufs.parser.ConfigurationParser;
import ru.sbrf.bh.bfs.ufs.type.poet.PoetFactory;

import java.io.IOException;

public class ProcessorTest {



    @Test
    public void newProcessorTestV6() throws IOException, TemplateException {
        Configuration configuration = ConfigurationParser.parse("/test_config.bfs");
        ru.sbrf.bh.bfs.generator.Processor processor = new ru.sbrf.bh.bfs.generator.Processor();

        processor.generateBfs(configuration,"src\\main\\resources\\templates\\6.9", PoetFactory.getApiPoetList(),PoetFactory.getServicePoetMap());
    }

    @Test
    public void newProcessorTestV7() throws IOException, TemplateException {
        Configuration configuration = ConfigurationParser.parse("/test_config.bfs");
        ru.sbrf.bh.bfs.generator.Processor processor = new ru.sbrf.bh.bfs.generator.Processor();

        processor.generateBfs(configuration,"src\\main\\resources\\templates\\7.0", PoetFactory.getApiPoetList(),PoetFactory.getServicePoetMap());
    }
}