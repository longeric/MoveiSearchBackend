package edu.pitt.ir.helpers;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Slf4j
public class AzureBlobBuilder {
    private CloudStorageAccount cloudStorageAccount = null;
    private CloudBlobClient cloudBlobClient = null;
    private CloudBlobContainer cloudBlobContainer = null;
    private CloudBlockBlob cloudBlockBlob = null;

    public AzureBlobBuilder setStorageAccount(String connectionString) {
        try {
            this.cloudStorageAccount = CloudStorageAccount.parse(connectionString);
        } catch (InvalidKeyException e) {
            log.error("invalid connectionString key");
        } catch (URISyntaxException e) {
            log.error("URI error");
        }

        return this;
    }

    public AzureBlobBuilder buildBolbClient() {
        this.cloudBlobClient = this.cloudStorageAccount.createCloudBlobClient();

        return this;
    }

    public AzureBlobBuilder getContainer(String containerName) {
        try {
            this.cloudBlobContainer = this.cloudBlobClient.getContainerReference(containerName);
        } catch (StorageException e) {
            log.error("invalid container name");
        } catch (URISyntaxException e) {
            log.error("URI error");
        }

        return this;
    }

    public AzureBlobBuilder build() {
        if (this.cloudBlobContainer == null) {
            throw new RuntimeException("Cannot build because missing configs");
        }

        return this;
    }

    public CloudBlockBlob getBlockBlobClient(String filename) {
        try {
            this.cloudBlockBlob = this.cloudBlobContainer.getBlockBlobReference(filename);
        } catch (StorageException e) {
            log.error("invalid file name");
        } catch (URISyntaxException e) {
            log.error("URI error");
        }

        return this.cloudBlockBlob;
    }

    public CloudBlobContainer getCloudBlobContainer() {
        if (this.cloudBlobContainer == null) {
            throw new RuntimeException("Cannot build because missing configs");
        }

        return this.cloudBlobContainer;
    }


}
