package ru.sbrf.bh.bfs;

import org.testng.annotations.Test;
import ru.sbrf.bh.bfs.model.Api;

import java.io.File;
import java.io.IOException;

//TODO: нельзя тесты использовать для генерации - тут надо ее проверять
public class DaPoetTest {
    File outputDir = new File("C:\\dev\\output\\smbaccounting\\src\\main\\java");

    @Test
    public void testRun() throws IOException {
        DaPoet poet = new DaPoet(outputDir);
        poet.run("SbrfGetLegalAccountBalanceDaService");
    }

    @Test
    public void testMakeSimple() throws IOException {
        DaPoet poet = new DaPoet(outputDir);
        poet.makeSimple(new Api()
                .setFgClass("ru.sbrf.bh.banking.product.smbaccounting.fg.SbrfGetLegalAccountBalanceFgService")
                .setDaClass("ru.sbrf.bh.banking.product.smbaccounting.da.SbrfGetLegalAccountBalanceDaService")
                .setService("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.SrvGetLegalAccountBalanceSyncClient")
                .setMethodName("call")
                .setRq("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto.LegalBalInqRqType")
                .setRs("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto.LegalBalInqRsType")
        );

    }
}