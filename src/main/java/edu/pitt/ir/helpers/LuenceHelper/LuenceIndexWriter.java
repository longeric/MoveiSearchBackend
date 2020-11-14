package edu.pitt.ir.helpers.LuenceHelper;


import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LuenceIndexWriter {

    private final List<Document> documentList;
    private IndexWriter indexWriter;
    private final AzureBlob azureBlob;
    public final RAMDirectory ramDirectory;


    public LuenceIndexWriter(AzureBlob azureBlob, RAMDirectory ramDirectory, Analyzer analyzer) {
        this.azureBlob = azureBlob;
        this.ramDirectory = ramDirectory;
        this.documentList = new ArrayList<>();

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try {
            this.indexWriter = new IndexWriter(ramDirectory, indexWriterConfig);
        } catch (IOException e) {
            log.error("unable to create index writer");
        }
    }

    public void createIndex () {

        List<String> allFileNames = this.azureBlob.getAllFileNames();

        this.getAllDocuments(allFileNames);

        this.documentList.parallelStream().forEach(document -> {
            try {
                this.indexWriter.addDocument(document);
            } catch (IOException e) {
                log.error("unable to write index into RAM");
            }
        });


        System.out.println(this.ramDirectory.getChildResources().size());

        this.close();
    }

    private void getAllDocuments(List<String> allFileNames) {
        allFileNames.stream().parallel().forEach(fileName -> {
            String content = this.azureBlob.readFiles(fileName);
            Document doc = new Document();
            TextField nameField = new TextField("name", fileName, Field.Store.YES);
            TextField contentField = new TextField("content", content, Field.Store.YES);
            doc.add(nameField);
            doc.add(contentField);

            this.documentList.add(doc);
        });
    }

    private void close() {
        try {
            this.indexWriter.close();
        } catch (IOException e) {
            log.error("unable to close writer");
        } finally {
            System.gc();
        }

    }

}
