package edu.pitt.ir.helpers.LuenceHelper;

import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class LuenceNormalizeText {

    @Value("${spring.azure.connectionKey}")
    private String connectionString;

    @Value("${spring.azure.containerName}")
    private String containerName;

    @Value("${spring.azure.containerAfterNormalizeName}")
    private String containerAfterNormalizeName;

    public void normalize() {
        AzureBlob azureBlob = new AzureBlob(this.connectionString, this.containerName);
        List<String> blobNames = azureBlob.getAllFileNames();

        int totalBlob = blobNames.size();

        AzureBlob uploadAzureBlob = new AzureBlob(this.connectionString, this.containerAfterNormalizeName);

        AtomicInteger atomicInteger = new AtomicInteger(0);
        blobNames.stream().parallel().forEach(name -> {
            String content = azureBlob.readFiles(name).replaceAll(",", "");

            List<String> tokens = new ArrayList<>();

            Analyzer analyzer = new StandardAnalyzer();

            try {
                TokenStream tokenStream  = analyzer.tokenStream(null, new StringReader(content));
                tokenStream.reset();
                while (tokenStream.incrementToken()) {
                    tokens.add(tokenStream.getAttribute(CharTermAttribute.class).toString());
                }
            } catch (IOException e) {
                throw new RuntimeException("cannot stream");
            }

            String fileContent = tokens.toString().replaceAll(",", "");
            uploadAzureBlob.uploadFiles(String.format("after_normalize_%s", name), fileContent.substring(1, fileContent.length() - 1));

            int curr = atomicInteger.incrementAndGet();

            if (curr % 10 == 0) {
                log.info(String.format("finishing uploading file %s percentage", curr * 100.0/totalBlob));
            }
        });
    }
}
