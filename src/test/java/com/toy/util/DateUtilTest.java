package com.toy.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DateUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testIsDateFormatExpected() {
		boolean isValid = DateUtil.isDateFormatExpected("2017-01-01", "yyyy-MM-dd");
		assertTrue(isValid);
		isValid = DateUtil.isDateFormatExpected("2017-01-01", "yyyyMMdd");
		assertFalse(isValid);
	}

	@Test
	public void testFormatDate() {
		String expectedDate = "20170101";
		String formattedDate = DateUtil.formatDate("2017-01-01", "yyyyMMdd");
		assertEquals(expectedDate, formattedDate);
		formattedDate = DateUtil.formatDate("201701", "yyyy-MM-dd");
		assertNull(formattedDate);
	}

	@Test
	public void testCompareDates() {
		String date1 = "2017-07-01";
		String date2 = "2017-11-11";
		int expectedResult = -1;
		int result = DateUtil.compareDates(date1, date2);
		assertEquals(expectedResult, result);
		expectedResult = 1;
		result = DateUtil.compareDates(date2, date1);
		assertEquals(expectedResult, result);
	}
}
