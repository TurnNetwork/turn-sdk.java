package com.bubble.codegen;

import com.bubble.TempFileProvider;
import org.junit.Test;


public class AbiTypesMapperGeneratorTest extends TempFileProvider {

    @Test
    public void testGeneration() throws Exception {
        com.bubble.codegen.AbiTypesMapperGenerator.main(new String[] { tempDirPath });
    }
}
