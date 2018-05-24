package ru.sbrf.bh.bfs;

import com.squareup.javapoet.ClassName;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.*;

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
        poet.makeSimple(
                ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.da","SbrfGetLegalAccountBalanceDaService")
                ,ClassName.get("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance","SrvGetLegalAccountBalanceSyncClient")
                ,ClassName.get("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto","LegalBalInqRqType")
                ,ClassName.get("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto","LegalBalInqRsType")
        );
    }
}