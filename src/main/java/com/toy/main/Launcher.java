package com.toy.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.toy.beans.Location;
import com.toy.model.WeatherDataGenerator;
import com.toy.util.CommonUtil;


/**
 * Starting point of application The main method accepts below arguments 1.
 * forcastStartDate - the data from which weather needs to be forecasted, format
 * should be yyyy-MM-dd 2. outputFilePath - the absolute file path to where the
 * forecasted information needs to be written 3. numDays (optional) - the number
 * of days (starting from forcastStartDate) to which weather needs to
 * forecasted. If this argument is not supplied, by default forecasting will be
 * done for 10 days.
 * 
 * @author Akhil
 *
 */
public class Launcher {

	public static void main(String[] args) throws IOException {
		// validate input arguments. If any of the arguments are invalid,
		// process will stop here.
		CommonUtil.validateInputArguments(args);
		// Populate input arguments in to a map
		Map<String, String> argsMap = CommonUtil.getArgsAsMap(args);

		// Populate locations data
		List<Location> locations = CommonUtil.populateLocationsData();
		
		WeatherDataGenerator.generateWeatherData(locations, argsMap);
		
		

		// take fort nights data for prediction - 2 weeks before and 2 weeks
		// after from last year..

	}

}
