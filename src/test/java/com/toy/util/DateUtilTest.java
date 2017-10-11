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

	@Test
	public void testSubYears() {
		String date = "2017-01-01";
		String expected = "2015-01-01";
		String result = DateUtil.subYears(date, 2);
		assertEquals(expected, result);
	}

	@Test
	public void testAddMonths() {
		String date = "2017-11-11";
		String expected = "2018-01-11";
		String result = DateUtil.addMonths(date, 2);
		assertEquals(expected, result);
	}

	@Test
	public void testSubMonths() {
		String date = "2017-11-11";
		String expected = "2017-07-11";
		String result = DateUtil.subMonths(date, 4);
		assertEquals(expected, result);
	}
	
	@Test
	public void testAddDays() {
		String date = "2017-11-11";
		String expected = "2017-11-20";
		String result = DateUtil.addDays(date, 9);
		assertEquals(expected, result);
	}
	
	@Test
	public void testSubDays() {
		String date = "2017-11-11";
		String expected = "2017-11-07";
		String result = DateUtil.subDays(date, 4);
		assertEquals(expected, result);
	}
	
	@Test
	public void testGetMonthFromDate() {
		String date = "2017-11-11";
		int expected = 11;
		int result = DateUtil.getMonthFromDate(date);
		assertEquals(expected, result);
	}
	
	
	
	
	
	

}
