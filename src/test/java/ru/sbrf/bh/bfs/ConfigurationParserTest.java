package ru.sbrf.bh.bfs;

import org.testng.Assert;
import org.testng.annotations.Test;
import ru.sbrf.bh.bfs.model.Adapter;
import ru.sbrf.bh.bfs.model.Api;
import ru.sbrf.bh.bfs.model.Configuration;
import ru.sbrf.bh.bfs.ufs.parser.ConfigurationParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Тест на парсинг конфигурационного файла
 * @author sbt-kotsyuruba-di
 * @since 28.05.2018
 */
public class ConfigurationParserTest {

    @Test
    public void parseTestPositive() throws IOException {
        Configuration configuration = ConfigurationParser.parse("/test_config.bfs");
        Assert.assertNotNull(configuration);

        HashMap<String, String> properties = configuration.getProperties();
        Assert.assertNotNull(properties);
        Assert.assertEquals("ru.sbrf.efs.app", properties.get("groupId"));
        Assert.assertEquals("ru.sbrf.bh.banking.product.smbaccounting", properties.get("artifactId"));

        HashMap<String, String> build = configuration.getBuild();
        Assert.assertNotNull(build);
        Assert.assertNotNull(build.get("outputDir"));

        List<Adapter> adapters = configuration.getAdapters();
        Assert.assertNotNull(adapters);
        Assert.assertEquals(10, adapters.size());

        List<Api> apis = configuration.getApis();
        Assert.assertNotNull(apis);
        Assert.assertEquals(9, apis.size());
    }

    /**
     * TODO: по идее при отсутствии файла должен кидаться IOException,
     * но внутри вызова antlr происходит вызов nio пакета Java, который кидает NPE
     */
    @Test(expectedExceptions = NullPointerException.class)
    public void parseTestNegative() throws IOException {
       ConfigurationParser.parse("/test1_config.bfs");
    }
}
