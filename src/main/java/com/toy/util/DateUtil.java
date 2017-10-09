package com.toy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import com.toy.constants.CommonConstants;

public class DateUtil {

	/**
	 * Check if the passed date is in the expected format.
	 * 
	 * @param date
	 *            the date
	 * @param expectedFormat
	 *            the format against which the date should be checked
	 * @return
	 */
	public static boolean isDateFormatValid(String date, String expectedFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(expectedFormat);
		try {
			dateFormat.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static String formatDate(String date, String format) {

		SimpleDateFormat df1 = new SimpleDateFormat(CommonConstants.FORECAST_DATE_FORMAT);
		Date formattedDate = null;
		try {
			formattedDate = df1.parse(date);
		} catch (ParseException e) {
			return null;
		}

		SimpleDateFormat df2 = new SimpleDateFormat(format);
		return df2.format(formattedDate);
	}

	/**
	 * This method compares the two dates passed.
	 * 
	 * @param date1
	 *            the date1
	 * @param date2
	 *            the date2
	 * @return 1 if date1 is greater than date2, returns -1 if date2 is greater
	 *         than date1 returns 0 if both dates are equal
	 */
	public static int compareDates(String date1, String date2) {
		LocalDate d1 = LocalDate.parse(date1);
		LocalDate d2 = LocalDate.parse(date2);
		if (d1.isAfter(d2)) {
			return 1;
		}
		if (d2.isAfter(d1)) {
			return -1;
		}
		return 0;
	}

	public static String subYears(String date, long years) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.minusYears(years).toString();
	}

	public static String addMonths(String date, long months) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.plusMonths(months).toString();
	}

	public static String subMonths(String date, long months) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.minusMonths(months).toString();
	}

	public static int getMonthFromDate(String date) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.getMonth().getValue();
	}
	
	public static String addDays(String date, long days) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.plusDays(days).toString();
	}
	
	public static String subDays(String date, long days) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.minusDays(days).toString();
	}

}
