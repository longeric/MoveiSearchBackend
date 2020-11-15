package edu.pitt.ir;

import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import edu.pitt.ir.helpers.LuenceHelper.LuenceIndexReader;
import edu.pitt.ir.helpers.LuenceHelper.LuenceIndexWriter;
import edu.pitt.ir.helpers.LuenceHelper.LuenceNormalizeText;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class IrApplication {
    @Value("${spring.azure.connectionKey}")
    private String connectionString;

    @Value("${spring.azure.containerAfterNormalizeName}")
    private String containerAfterNormalizeName;

    @Value("${spring.azure.containerName}")
    private String containerName;

    private static final Analyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);


    public static void main(String[] args) {
        SpringApplication.run(IrApplication.class, args);
    }

    /**
     * run once before start server
     */

//    @PostConstruct
//    public void normalize() {
//        LuenceNormalizeText luenceNormalizeText = new LuenceNormalizeText(this.connectionString, this.containerName,
//                this.connectionString, this.containerAfterNormalizeName, analyzer);
//        luenceNormalizeText.normalize();
//    }

    @PostConstruct
    public void initialize() {
        RAMDirectory ramDirectory = new RAMDirectory();

        AzureBlob azureBlobAfterNormalize = new AzureBlob(this.connectionString, this.containerAfterNormalizeName);
        AzureBlob azureBlob = new AzureBlob(this.connectionString, this.containerName);
        LuenceIndexWriter luenceIndexWriter = new LuenceIndexWriter(azureBlobAfterNormalize, ramDirectory, analyzer);
        luenceIndexWriter.createIndex();

        LuenceIndexReader.getInstance(azureBlob, ramDirectory, analyzer);
    }
}
