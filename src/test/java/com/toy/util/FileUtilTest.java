package com.toy.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.toy.constants.CommonConstants;

public class FileUtilTest {

	private String invalidFileName;

	@Before
	public void setUp() throws Exception {
		invalidFileName = "locations_1212.txt";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadFile() throws IOException {
		String content = FileUtil.readFile(CommonConstants.LOCATIONS_FILE);
		assertNotNull(content);
		content = FileUtil.readFile(invalidFileName);
		assertNull(content);
	}

}
