package edu.pitt.ir;

import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import edu.pitt.ir.helpers.LuceneHelper.LuceneIndexReader;
import edu.pitt.ir.helpers.LuceneHelper.LuceneIndexWriter;
import edu.pitt.ir.helpers.LuceneHelper.LuceneNormalizeText;
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
//        LuceneNormalizeText luceneNormalizeText = new LuceneNormalizeText(this.connectionString, this.containerName,
//                this.connectionString, this.containerAfterNormalizeName, analyzer);
//        luceneNormalizeText.normalize();
//    }

    @PostConstruct
    public void initialize() {
        RAMDirectory ramDirectory = new RAMDirectory();

        AzureBlob azureBlobAfterNormalize = new AzureBlob(this.connectionString, this.containerAfterNormalizeName);
        AzureBlob azureBlob = new AzureBlob(this.connectionString, this.containerName);
        LuceneIndexWriter luceneIndexWriter = new LuceneIndexWriter(azureBlobAfterNormalize, ramDirectory, analyzer);
        luceneIndexWriter.createIndex();

        LuceneIndexReader.getInstance(azureBlob, ramDirectory, analyzer);
    }
}
