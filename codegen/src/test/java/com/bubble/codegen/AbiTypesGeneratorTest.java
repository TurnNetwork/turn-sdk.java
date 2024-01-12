package com.bubble.codegen;

import com.bubble.TempFileProvider;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class AbiTypesGeneratorTest extends TempFileProvider {

    @Test
    public void testGetPackageName() {
        assertThat(com.bubble.codegen.AbiTypesGenerator.getPackageName(String.class), is("java.lang"));
    }

    @Test
    public void testCreatePackageName() {
        assertThat(com.bubble.codegen.AbiTypesGenerator.createPackageName(String.class), is("java.lang.generated"));
    }

    @Test
    public void testGeneration() throws Exception {
        com.bubble.codegen.AbiTypesGenerator.main(new String[] { tempDirPath });
    }
}
