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
	 * @return true if data is in expected format, else false;
	 */
	public static boolean isDateFormatExpected(String date, String expectedFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(expectedFormat);
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(date);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static String formatDate(String date, String format) {
		SimpleDateFormat df1 = new SimpleDateFormat(CommonConstants.DATE_FORMAT_YYYY_MM_DD);
		df1.setLenient(false);
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
	 * @return 1 if date1 is after date2; return -1 if date1 is before date2;
	 *         return 0 if both dates are equal
	 */
	public static int compareDates(String date1, String date2) {
		LocalDate d1 = LocalDate.parse(date1);
		LocalDate d2 = LocalDate.parse(date2);
		if (d1.isAfter(d2)) {
			return 1;
		}
		if (d1.isBefore(d2)) {
			return -1;
		}
		return 0;
	}

	/**
	 * Subtract specified number of years from the date supplied
	 * 
	 * @param date
	 *            the date
	 * @param years
	 *            the number of years
	 * @return the new date
	 */
	public static String subYears(String date, long years) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.minusYears(years).toString();
	}

	/**
	 * Add specified number of months to the date supplied
	 * 
	 * @param date
	 *            the date
	 * @param months
	 *            the number of months
	 * @return the new date
	 */
	public static String addMonths(String date, long months) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.plusMonths(months).toString();
	}

	/**
	 * Subtract specified number of months from the date supplied
	 * 
	 * @param date
	 *            the date
	 * @param months
	 *            the number of months
	 * @return the new date
	 */
	public static String subMonths(String date, long months) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.minusMonths(months).toString();
	}

	/**
	 * Get month from a date supplied
	 * 
	 * @param date
	 *            the date
	 * @return the month part
	 */
	public static int getMonthFromDate(String date) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.getMonth().getValue();
	}

	/**
	 * Add a specific number of days to date supplied
	 * 
	 * @param date
	 *            the date
	 * @param days
	 *            the number of days
	 * @return the new date
	 */
	public static String addDays(String date, long days) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.plusDays(days).toString();
	}

	/**
	 * Subtract a specific number of days to date supplied
	 * 
	 * @param date
	 *            the date
	 * @param days
	 *            the number of days
	 * @return the new date
	 */
	public static String subDays(String date, long days) {
		LocalDate d1 = LocalDate.parse(date);
		return d1.minusDays(days).toString();
	}

}
