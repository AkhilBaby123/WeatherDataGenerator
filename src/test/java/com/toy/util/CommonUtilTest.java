package com.toy.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.toy.beans.Location;
import com.toy.beans.Position;
import com.toy.beans.ZoneId;

/**
 * This class implements test cases for CommonUtil class
 * 
 * @author Akhil
 *
 */
public class CommonUtilTest {

	private String[] validArguments;
	private String[] invalidArguments;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		validArguments = new String[] { "2017-12-12", "test", "3" };
		invalidArguments = new String[] { "20170202", "test" };
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInputArguments() {
		boolean isValid = CommonUtil.validateInputArguments(validArguments);
		assertTrue(isValid);
		isValid = CommonUtil.validateInputArguments(invalidArguments);
		assertFalse(isValid);
	}

	@Test
	public void testPopulateLocation() throws IOException {
		List<Location> locations = CommonUtil.populateLocationsData();
		assertFalse(locations.isEmpty());
	}

	@Test
	public void testGetZoneIdsAsMap() throws IOException {
		Map<String, ZoneId> map = CommonUtil.getZoneIdsAsMap();
		assertFalse(map.isEmpty());
	}

	@Test
	public void testGetPositionDataAsMap() throws IOException {
		Map<String, Position> map = CommonUtil.getPositionsDataAsMap();
		assertFalse(map.isEmpty());
	}

}
