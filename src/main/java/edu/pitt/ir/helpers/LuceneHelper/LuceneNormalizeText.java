package edu.pitt.ir.helpers.LuceneHelper;

import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LuceneNormalizeText {

    private final AzureBlob azureSearchBlob;
    private final AzureBlob azureUploadBlob;
    private final Analyzer analyzer;

    public LuceneNormalizeText(String search_connectionString, String search_containerName,
                               String upload_connectionString, String upload_containerName,
                               Analyzer analyzer) {
        this.azureSearchBlob = new AzureBlob(search_connectionString, search_containerName);
        this.azureUploadBlob = new AzureBlob(upload_connectionString, upload_containerName);
        this.analyzer = analyzer;
    }

    public void normalize() {
        List<String> blobNames = this.azureSearchBlob.getAllFileNames();

        int totalBlob = blobNames.size();

        AtomicInteger atomicInteger = new AtomicInteger(0);
        blobNames.stream().parallel().forEach(name -> {
            String titleAndContent = this.azureSearchBlob.readFiles(name);
            String content = titleAndContent.split("\n")[1];

            List<String> tokens = new ArrayList<>();

            try {
                TokenStream tokenStream  = this.analyzer.tokenStream("content", new StringReader(content));
                tokenStream.reset();
                while (tokenStream.incrementToken()) {
                    tokens.add(tokenStream.getAttribute(CharTermAttribute.class).toString());
                }

                tokenStream.close();
            } catch (IOException e) {
                throw new RuntimeException("cannot stream");
            }

            String fileContent = tokens.toString().replaceAll(",", "");
            this.azureUploadBlob.uploadFiles(String.format("after_normalize_%s", name), fileContent.substring(1, fileContent.length() - 1));

            int curr = atomicInteger.incrementAndGet();

            if (curr % 100 == 0) {
                log.info(String.format("finishing uploading file %s percentage", curr * 100.0/totalBlob));
            }
        });
    }
}
