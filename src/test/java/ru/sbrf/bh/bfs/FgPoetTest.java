package ru.sbrf.bh.bfs;

import com.squareup.javapoet.ClassName;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class FgPoetTest {
    File outputDir = new File("C:\\dev\\output\\smbaccounting\\src\\main\\java");

    @Test
    public void testRun() throws IOException {
        FgPoet poet = new FgPoet(outputDir);
        poet.run("SbrfGetLegalAccountBalanceFgService");
    }

    @Test
    public void testMakeSimple() throws IOException {
        FgPoet poet = new FgPoet(outputDir);
        poet.makeSimple(new Api()
                .setFgClass("ru.sbrf.bh.banking.product.smbaccounting.fg.SbrfGetLegalAccountBalanceFgService")
                .setDaClass("ru.sbrf.bh.banking.product.smbaccounting.da.SbrfGetLegalAccountBalanceDaService")
                .setMethodName("call")
                .setRq("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto.LegalBalInqRqType")
                .setRs("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto.LegalBalInqRsType")
        );
    }
}