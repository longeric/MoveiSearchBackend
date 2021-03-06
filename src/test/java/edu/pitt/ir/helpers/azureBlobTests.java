package edu.pitt.ir.helpers;

import edu.pitt.ir.helpers.AzureHelper.AzureBlob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class azureBlobTests {

    @Value("${spring.azure.test.connectionKey}")
    private String connectionKey;

    @Value("${spring.azure.test.containerName}")
    private String containerName;

    AzureBlob azureBlob;

    @Before
    public void set_up() {
        this.azureBlob = new AzureBlob(this.connectionKey, this.containerName);
    }

    @Test
    public void test_ReadFiles() {
        String actualResult = this.azureBlob.readFiles("test.txt");

        String expectedResult = "this is a test file\n" + "\n" + "this is with double line";

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void test_UploadFiles() {
        boolean expectedResult = this.azureBlob.uploadFiles("test-upload.txt", "test");

        Assert.assertTrue(expectedResult);
    }

    @Test
    public void test_GetAllFileNames() {
        List<String> actualResult = this.azureBlob.getAllFileNames();

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("test-upload.txt");
        expectedResult.add("test.txt");

        Assert.assertEquals(expectedResult, actualResult);
    }
}
