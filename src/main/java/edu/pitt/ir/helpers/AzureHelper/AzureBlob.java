package edu.pitt.ir.helpers.AzureHelper;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class AzureBlob {

    AzureBlobBuilder azureBlobBuilder;

    public AzureBlob(String connectionString, String containerName) {
        this.azureBlobBuilder = new AzureBlobBuilder()
                .setStorageAccount(connectionString)
                .buildBolbClient()
                .getContainer(containerName)
                .build();
    }

    public String readFiles(String fileName) {
        CloudBlockBlob azureBlob = this.azureBlobBuilder.getBlockBlobClient(fileName);
        String res = "cannot find fine";

        try {
            res = Arrays.toString(azureBlob.downloadText().split("\\W+"));
        } catch (StorageException e) {
            log.error("invalid file name");
        } catch (IOException e) {
            log.error("error with IO");
        }

        return res;
    }

    public boolean uploadFiles(String fileName, String content) {
        CloudBlockBlob azureBlob = this.azureBlobBuilder.getBlockBlobClient(fileName);
        try {
            azureBlob.uploadText(content);
            return true;
        } catch (StorageException e) {
            log.error("invalid file name");
        } catch (IOException e) {
            log.error("cannot upload");
        }

        return false;
    }

    public List<String> getAllFileNames() {
        CloudBlobContainer cloudBlobContainer = this.azureBlobBuilder.getCloudBlobContainer();
        ArrayList<String> fileNames = new ArrayList<>();
        for (ListBlobItem listBlobItem : cloudBlobContainer.listBlobs()) {
            CloudBlob cloudBlob = (CloudBlob) listBlobItem;

            fileNames.add(cloudBlob.getName());
        }

        return fileNames;
    }
}
