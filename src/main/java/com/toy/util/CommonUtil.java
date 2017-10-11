package com.toy.util;

import java.io.IOException;
import java.time.LocalDate;
import com.toy.beans.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.toy.beans.Location;
import com.toy.beans.Position;
import com.toy.constants.CommonConstants;

public class CommonUtil {
	private static final Logger logger = Logger.getLogger(CommonUtil.class);

	/**
	 * validates the input arguments passed to the main function.
	 * 
	 * @param args
	 *            the input arguments
	 * @return true if arguments are valid, else false
	 */
	public static boolean validateInputArguments(String[] args) {

		if (args == null) {
			logger.error("Input Argument cannot be NULL.. <CommonUtil.validateInputArguments>");
			System.exit(0);
		}

		int length = args.length;
		if (length != 2 && length != 3) {
			logger.error(
					"Invalid number of arguments supplied. Usage: Launcher <forcastStartDate> <outputFilePath> <numDays(optional)>");
			return false;
		}

		String forecastDate = args[0];
		// check whether the forecast date is in expected format
		boolean isDateFormatValid = DateUtil.isDateFormatExpected(forecastDate, CommonConstants.DATE_FORMAT_YYYY_MM_DD);
		if (!isDateFormatValid) {
			logger.error("Forcast date is not in expected format...Should be in yyyy-MM-dd format");
			return false;
		}

		// Forecast date should be greater than current date
		String currentDate = LocalDate.now(java.time.ZoneId.of(CommonConstants.ZONEID_PERTH)).toString();
		int result = DateUtil.compareDates(forecastDate, currentDate);
		if (result <= 0) {
			logger.error("Forcast date should be a future date.");
			return false;
		}

		if (args.length == 3) {
			try {
				Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				logger.error("Num of Days should be numeric..");
				return false;
			}
		}
		return true;
	}

	/**
	 * Store input arguments as a key value map
	 * 
	 * @param args
	 *            the input arguments
	 * @return the map representing arguments.
	 */
	public static Map<String, String> getArgsAsMap(String[] args) {
		if (args == null) {
			logger.error("Input Argument cannot be NULL.. <CommonUtil.validateInputArguments>");
			System.exit(0);
		}
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put(CommonConstants.FORECAST_START_DATE, args[0]);
		argsMap.put(CommonConstants.OUTPUTFILEPATH, args[1]);
		int numDays = args.length > 2 ? Integer.parseInt(args[2]) : CommonConstants.DEFAULT_NUM_DAYS;
		// forecasting can be done for up to 14 days from the start date..If
		// number of days supplied is more than 14, forecasting will be done for
		// up to 14 days.
		numDays = (numDays > CommonConstants.MAX_FORCAST_SIZE) ? CommonConstants.MAX_FORCAST_SIZE : numDays;

		argsMap.put(CommonConstants.NUM_DAYS, Integer.toString(numDays));
		return argsMap;
	}

	/**
	 * This method populates location details.
	 * 
	 * @throws IOException
	 * @return list of locations
	 */
	public static List<Location> populateLocationsData() throws IOException {
		String locationData = FileUtil.readFile(CommonConstants.LOCATIONS_FILE);
		if (locationData == null) {
			logger.error("Error Reading location data.. Exiting");
			System.exit(0);
		}
		return Location.loadLocations(locationData);

	}

	/**
	 * Check if the input string passed is NULL/empty
	 * 
	 * @param st
	 *            the input string
	 * @return true if the string is null/empty, else false
	 */
	public static boolean isNullOrEmpty(String st) {
		if (st == null || st.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * This method reads the Zone Id file (static file) and returns a key value
	 * representing Zone Ids read. The key will be city and value will be the
	 * Zone ID for the city
	 * 
	 * @return the Zone Ids read as a key value pair. Key will be city and value
	 *         will be zoneId representing the data read
	 * @throws IOException
	 */
	public static Map<String, ZoneId> getZoneIdsAsMap() throws IOException {
		String zoneData = FileUtil.readFile(CommonConstants.FILE_NAME_ZONE_IDS);
		List<ZoneId> zoneIds = ZoneId.load(zoneData);
		Map<String, ZoneId> map = new HashMap<String, ZoneId>();
		Iterator<ZoneId> it = zoneIds.iterator();
		while (it.hasNext()) {
			ZoneId zoneId = it.next();
			map.put(zoneId.getCity(), zoneId);
		}
		return map;
	}

	/**
	 * This method reads the positions file (static file) and returns a key
	 * value pair representing the positions read. The key will the city and
	 * value will the position details for the city
	 * 
	 * @return the positions data read as a key value pair; key will be city and
	 *         value will the position details corresponding to the city
	 * @throws IOException
	 */
	public static Map<String, Position> getPositionsDataAsMap() throws IOException {
		String positionData = FileUtil.readFile(CommonConstants.POSITIONS_FILE);
		List<Position> positions = Position.loadPositions(positionData);
		Map<String, Position> map = new HashMap<String, Position>();
		Iterator<Position> it = positions.iterator();
		while (it.hasNext()) {
			Position pos = it.next();
			map.put(pos.getCity(), pos);
		}
		return map;
	}

	/**
	 * Round of the double value to 2 decimal places
	 * 
	 * @param value
	 *            the value to round of
	 * @return the round of value
	 */
	public static double roundOf(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

}
