package edu.pitt.ir;

import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import edu.pitt.ir.helpers.LuenceHelper.LuenceIndexReader;
import edu.pitt.ir.helpers.LuenceHelper.LuenceIndexWriter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.jvm.hotspot.debugger.remote.amd64.RemoteAMD64Thread;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class IrApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrApplication.class, args);
    }

    /**
     * run once before start server
     */
//    @Autowired
//    NormalizeText normalizeText;
//
//    @PostConstruct
//    public void normalize() {
//        this.normalizeText.normalize();
//    }


    @Value("${spring.azure.connectionKey}")
    private String connectionString;

    @Value("${spring.azure.containerAfterNormalizeName}")
    private String containerName;

    @PostConstruct
    public void findContent() {
        Analyzer analyzer = new StandardAnalyzer();
        RAMDirectory ramDirectory = new RAMDirectory();

        AzureBlob azureBlob = new AzureBlob(this.connectionString, this.containerName);
        LuenceIndexWriter luenceIndexWriter = new LuenceIndexWriter(azureBlob, ramDirectory, analyzer);
        luenceIndexWriter.createIndex();

        LuenceIndexReader.getInstance(ramDirectory, analyzer);
    }
}
