package com.toy.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.toy.beans.Location;
import com.toy.constants.CommonConstants;
import com.toy.helper.WeatherDataGenerator;
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
 * (prior to forecasting data and after forecasting date) will be supplied to
 * the ARIMA model as input. The historical weather data has been pulled from
 * BOM site (http://www.bom.gov.au/climate/dwo)
 * 
 * The output of this program will be forecasted weather data - temperature,
 * pressure and relative humidity for different cities across Australia
 * 
 * The location and position information is read from static files
 * (locations.txt and positions.txt)
 * 
 * Program uses log4j for logging
 * 
 * 
 * @author Akhil
 *
 */
public class Launcher {

	private static final Logger logger = Logger.getLogger(Launcher.class);

	public static void main(String[] args) throws IOException {
		// validate input arguments. If any of the arguments are invalid,program
		// will stop here.
		boolean isValid = CommonUtil.validateInputArguments(args);
		if (!isValid) {
			logger.error("Input arguments not valid.. Exiting..");
			System.exit(0);
		}

		// Populate input arguments in to a map
		Map<String, String> argsMap = CommonUtil.getArgsAsMap(args);
		logger.info("Starting application with :- \n	Forecast StartDate:- " + args[0] + "\n	Output File Path:- "
				+ args[1] + "\n	Num days:- " + argsMap.get(CommonConstants.NUM_DAYS));
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
			logger.info("Starting weather prediction for " + location.getCityName());
			weatherDataGenerator = new WeatherDataGenerator(location);
			weatherDataGenerator.generateForcastData();
			logger.info("Writing forecasted results for " + location.getCityName() + " to output file..");
			FileUtil.writeToFile(outFileName, weatherDataGenerator.getForecastData(), true);
		}
	}
}
