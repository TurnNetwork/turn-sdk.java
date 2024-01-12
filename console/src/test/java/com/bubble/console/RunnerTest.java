package com.bubble.console;

import com.bubble.TempFileProvider;
import com.bubble.utils.Strings;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Test Java wrapper source code generator for Solidity ABI format.
 * 
 * @author lhdeng
 *
 */
public class RunnerTest extends TempFileProvider {
	private Logger logger = LoggerFactory.getLogger(RunnerTest.class);
	private String bin = "contract/HumanStandardToken.bin";
	private String abi = "contract/HumanStandardToken.abi";
	
	private String wasmbin = "contract/ContractDistory.wasm";
	private String wasmabi = "contract/ContractDistory.abi.json";

	@Test
	public void generateJavaCode() {
		String binPath = RunnerTest.class.getClassLoader().getResource(bin).getPath();
		String abiPath = RunnerTest.class.getClassLoader().getResource(abi).getPath();

		String outputPath = tempDirPath;
		// String outputPath = System.getProperty("user.dir").replace("console", "core") + "/src/test/java/";
		String packageName = "com.bubble.sdk.contracts";

		String[] params = { "solidity", "generate", binPath, abiPath, "-o", outputPath, "-p", packageName };
		try {
			Runner.main(params);
		} catch (Exception e) {
			logger.error("Failed to generate Java code: " + e.getMessage(), e);
		}

		String sourceFile = tempDirPath + File.separator + packageName.replace('.', File.separatorChar) + File.separator
				+ Strings.capitaliseFirstLetter("HumanStandardToken") + ".java";

		boolean condition = new File(sourceFile).exists();

		Assert.assertTrue("Java wrapper source code generator for Solidity ABI format error.", condition);
	}
	
	@Test
	public void generateWasmJavaCode() {
		String binPath = RunnerTest.class.getClassLoader().getResource(wasmbin).getPath();
		String abiPath = RunnerTest.class.getClassLoader().getResource(wasmabi).getPath();

		String outputPath = tempDirPath;
		// String outputPath = System.getProperty("user.dir").replace("console", "core") + "/src/test/java/";
		String packageName = "com.bubble.sdk.contracts";

		String[] params = { "wasm", "generate", binPath, abiPath, "-o", outputPath, "-p", packageName };
		try {
			Runner.main(params);
		} catch (Exception e) {
			logger.error("Failed to generate Java code: " + e.getMessage(), e);
		}

		String sourceFile = tempDirPath + File.separator + packageName.replace('.', File.separatorChar) + File.separator
				+ Strings.capitaliseFirstLetter("ContractDistory") + ".java";

		boolean condition = new File(sourceFile).exists();

		Assert.assertTrue("Java wrapper source code generator for Solidity ABI format error.", condition);
	}
}
