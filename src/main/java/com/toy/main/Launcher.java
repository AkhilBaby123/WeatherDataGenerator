package com.toy.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.toy.beans.Location;
import com.toy.constants.CommonConstants;
import com.toy.model.WeatherDataGenerator;
import com.toy.util.CommonUtil;
import com.toy.util.FileUtil;

/**
 * 
 * Starting point of the application
 * 
 * The main method accepts 3 parameters as input arguments; two of them are
 * mandatory and one is optional. Below are the list of arguments.
 * 
 * 1). ForecaststartDate - the date from which weather needs to be forecasted.
 * It should be in yyyy-MM-dd format 2).OutputFilePath - The absolute file path
 * to where the output data will be written 3). NumDays - indicates the number
 * of days (starting from forecast start date) for which the forecasting needs
 * to be done. This argument is optional; if the argument is not supplied,
 * program will assume 10 days as default
 * 
 * This program uses ARIMA model to forecast the weather. One fort nights data
 * (prior to forecasting data and after forecasting date) has been supplied to
 * the ARIMA model as input. The historical data has been pulled from bom site
 * (http://www.bom.gov.au/climate/dwo)
 * 
 * The output of this program will be forecasted weather data for different
 * locations.
 * 
 * 
 * @author Akhil
 *
 */
public class Launcher {

	public static void main(String[] args) throws IOException {
		// validate input arguments. If any of the arguments are invalid,process
		// will stop here.
		boolean isValid = CommonUtil.validateInputArguments(args);
		if (!isValid) {
			System.exit(0);
		}

		// Populate input arguments in to a map
		Map<String, String> argsMap = CommonUtil.getArgsAsMap(args);
		String outFileName = argsMap.get(CommonConstants.OUTPUTFILEPATH);
		// Delete output file if already exists
		FileUtil.deleteFile(outFileName);
		// Set general parameters for weather data generator
		WeatherDataGenerator.forecastStartDate = argsMap.get(CommonConstants.FORECAST_START_DATE);
		WeatherDataGenerator.numDays = Integer.parseInt(argsMap.get(CommonConstants.NUM_DAYS));
		WeatherDataGenerator.posMap = CommonUtil.getPositionsDataAsMap();
		WeatherDataGenerator.zoneIdsMap = CommonUtil.getZoneIdsAsMap();
		WeatherDataGenerator weatherDataGenerator = null;
		// Pull locations
		List<Location> locations = CommonUtil.populateLocationsData();
		for (Location location : locations) {
			weatherDataGenerator = new WeatherDataGenerator(location);
			weatherDataGenerator.generateForcastData();
			FileUtil.writeToFile(outFileName, weatherDataGenerator.getForecastData(), true);
		}
	}

}
