package com.toy.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.toy.beans.Location;
import com.toy.beans.Position;
import com.toy.constants.CommonConstants;

public class CommonUtil {

	/**
	 * validates the input arguments passed to the main function.
	 * 
	 * @param args
	 *            the input arguments
	 */
	public static void validateInputArguments(String[] args) {
		if (args.length != (2 | 3)) {
			System.out.println(
					"Invalid number of arguments supplied. Usage -> Launcher <forcastStartDate> <outputFilePath> <numDays(optional)>");
			System.exit(0);
		}

		// check whether the forecast date is in expected format
		boolean isDateFormatValid = DateUtil.isDateFormatValid(args[0], CommonConstants.FORECAST_DATE_FORMAT);
		if (!isDateFormatValid) {
			System.out.println("Forcast date format is invalid...Should be in yyyy-MM-dd format");
			System.exit(0);
		}

		// Forecast date should be greater than current date
		String currentDate = LocalDate.now(ZoneId.of("Australia/Perth")).toString();
		int result = DateUtil.compareDates(args[0], currentDate);
		if (result <= 0) {
			System.out.println("Forcast date should be a future date.");
			System.exit(0);
		}
		
		//TODO - limit number of days to 20
	}

	/**
	 * Generate a map representing arguments
	 * 
	 * @param args
	 *            the input arguments
	 * @return the map representing arguments.
	 */
	public static Map<String, String> getArgsAsMap(String[] args) {
		Map<String, String> argsMap = new HashMap<String, String>();
		argsMap.put(CommonConstants.FORCAST_DATE, args[0]);
		argsMap.put(CommonConstants.OUTPUTFILEPATH, args[1]);
		int numDays = args.length > 2 ? Integer.parseInt(args[2]) : CommonConstants.DEFAULT_NUM_DAYS;
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
			System.out.println("Error Reading location data.. Exiting");
			System.exit(0);
		}
		return Location.loadLocations(locationData);

	}

	/**
	 * This method populates positions details.
	 * 
	 * @throws IOException
	 * @return list of locations
	 */
	public static List<Position> populatePositionsData() throws IOException {
		String positionData = FileUtil.readFile(CommonConstants.POSITIONS_FILE);
		if (positionData == null) {
			System.out.println("Error Reading position data.. Exiting");
			System.exit(0);
		}
		return Position.loadPositions(positionData);
	}
	
	public static boolean isNullOrEmpty(String st){
		if(st == null || st.isEmpty()){
			return true;
		}
		return false;
	}

}
