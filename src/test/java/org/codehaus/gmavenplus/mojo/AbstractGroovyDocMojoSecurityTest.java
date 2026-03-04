package org.codehaus.gmavenplus.mojo;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class AbstractGroovyDocMojoSecurityTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private AbstractGroovyDocMojo mojo;
    private File testStylesheet;
    private File outputDir;

    @Before
    public void setup() throws IOException {
        testStylesheet = temporaryFolder.newFile("test.css");
        outputDir = temporaryFolder.newFolder("output");

        mojo = new AbstractGroovyDocMojo() {
            @Override
            public void execute() {}
        };
        mojo.stylesheetFile = testStylesheet;
        mojo.setLog(new SystemStreamLog());
    }

    @Test
    public void testCopyStylesheet() throws IOException {
        String content = "body { color: red; }\n.class { margin: 0; }";
        Files.write(testStylesheet.toPath(), content.getBytes(StandardCharsets.UTF_8));

        mojo.copyStylesheet(outputDir);

        File outputFile = new File(outputDir, "stylesheet.css");
        Assert.assertTrue("Output file should exist", outputFile.exists());
        List<String> lines = Files.readAllLines(outputFile.toPath(), StandardCharsets.UTF_8);
        Assert.assertEquals("body { color: red; }", lines.get(0));
        Assert.assertEquals(".class { margin: 0; }", lines.get(1));
    }

    @Test
    public void testCopyStylesheetWithEncoding() throws IOException {
        mojo.stylesheetEncoding = "UTF-16";
        String content = "body { color: blue; }";
        Files.write(testStylesheet.toPath(), content.getBytes(StandardCharsets.UTF_16));

        mojo.copyStylesheet(outputDir);

        File outputFile = new File(outputDir, "stylesheet.css");
        Assert.assertTrue("Output file should exist", outputFile.exists());
        byte[] bytes = Files.readAllBytes(outputFile.toPath());
        String actualContent = new String(bytes, StandardCharsets.UTF_16);
        Assert.assertTrue(actualContent.contains("body { color: blue; }"));
    }
}
