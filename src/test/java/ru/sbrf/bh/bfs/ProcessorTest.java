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
        p.init("/smbaccounting2.bfs");

        p.prepareTargetDir();
        File outputDir = p.javaPath();

        p.createPom();

        ClassName daClass = ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.da","SbrfGetLegalAccountBalanceDaService");
        ClassName rq = ClassName.get("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto","LegalBalInqRqType");
        ClassName rs = ClassName.get("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance.dto","LegalBalInqRsType");
        ClassName service = ClassName.get("ru.sbrf.ufs.integration.fs.srvgetlegalaccountbalance","SrvGetLegalAccountBalanceSyncClient");

        DaPoet daPoet = new DaPoet(outputDir);
        daPoet.makeSimple(
                daClass
                ,service
                ,rq
                ,rs
        );

        FgPoet poet = new FgPoet(outputDir);
        ClassName fgClass = ClassName.get("ru.sbrf.bh.banking.product.smbaccounting.fg", "SbrfGetLegalAccountBalanceFgService");
        poet.makeSimple(fgClass
                ,daClass
                ,"call"
                ,rq
                ,rs
        );
    }
}