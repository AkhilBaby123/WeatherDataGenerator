package com.toy.model;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.toy.beans.BomObservation;
import com.toy.beans.Location;
import com.toy.beans.Position;
import com.toy.beans.ZoneId;
import com.toy.constants.CommonConstants;
import com.toy.util.CommonUtil;
import com.toy.util.DateUtil;
import com.toy.util.UrlUtil;

/**
 * Class responsible for building input (historical) values to be used for model
 * building. For each of the locations, this class builds historical data and
 * pass the same to weather predictor for prediction
 * 
 * This class is also responsible for generating the output data in the
 * predefined format
 * 
 * @author Akhil
 *
 */
public class WeatherDataGenerator {
	public Location location;
	public static String forecastStartDate;
	public static int numDays;
	public static Map<String, Position> posMap;
	public static Map<String, ZoneId> zoneIdsMap;
	private StringBuilder forecastData;

	/**
	 * Initialize weather data generator
	 * 
	 * @param location
	 *            the location object
	 */
	public WeatherDataGenerator(Location location) {
		this.location = location;
	}

	/**
	 * This method predict temperature, pressure and humidity data based on
	 * historical data. Historical data is pulled from BOM site. For prediction,
	 * it make use of a popular forecasting model called ARIMA.
	 * 
	 * @throws IOException
	 */
	public void generateForcastData() throws IOException {
		forecastData = new StringBuilder();
		Map<String, BomObservation> obsMap = getBomObservationsAsMap(location.getBomCode(), forecastStartDate);
		String city = location.getCityName();
		for (int i = 0; i < numDays; i++) {
			String forcastDate = DateUtil.addDays(forecastStartDate, i);
			// TODO- remove
			System.out.println("Forecast Date: " + forcastDate);
			double[] tempData = getHistoricalTempData(forcastDate, obsMap);
			System.out.println("Print Temperature data...");
			printData(tempData);
			double[] humidityData = getHistoricalHumidityData(forcastDate, obsMap);
			System.out.println("Print Humidity data...");
			printData(humidityData);
			double[] pressureData = getHistoricalPressureData(forcastDate, obsMap);
			System.out.println("Print Pressure data...");
			printData(pressureData);
			double[] forecastTempData = WeatherPredictor.forcast(tempData, CommonConstants.FORCAST_SIZE_ONE);
			double[] forecastHumidityData = WeatherPredictor.forcast(humidityData, CommonConstants.FORCAST_SIZE_ONE);
			double[] forecastPressureData = WeatherPredictor.forcast(pressureData, CommonConstants.FORCAST_SIZE_ONE);
			// TODO -- populate condition
			String condition = "";
			String out = generateOutput(city, forcastDate, condition, forecastTempData, forecastPressureData,
					forecastHumidityData);
			forecastData.append(out);
			forecastData.append(CommonConstants.NEWLINE);

		}
	}

	public void printData(double[] data) {
		int length = data.length;
		int i = 0;
		while (i < length) {
			System.out.println(data[i]);
			i++;
		}
	}

	/**
	 * This method reads BOM observations from BOM site.Based on forecast date
	 * argument, it will read data for three months - data for forecast month;
	 * one month prior to forecast month and one month after forecast month.
	 * 
	 * @param bomCode
	 *            the BOM code
	 * @param forecastStartDate
	 *            the forecast start date
	 * @return the data read as a key value pair. The key will be date for which
	 *         the observation corresponds to and value will the corresponding
	 *         BOM data
	 * @throws IOException
	 */
	private static Map<String, BomObservation> getBomObservationsAsMap(String bomCode, String forecastStartDate)
			throws IOException {
		StringBuilder bomData = new StringBuilder();
		bomData.append(UrlUtil.readUrl(getBomUrl(bomCode, forecastStartDate)));
		bomData.append(UrlUtil.readUrl(getBomUrl(bomCode, DateUtil.subMonths(forecastStartDate, 1))));
		bomData.append(UrlUtil.readUrl(getBomUrl(bomCode, DateUtil.addMonths(forecastStartDate, 1))));
		List<BomObservation> observations = BomObservation.load(bomData.toString());
		Map<String, BomObservation> map = new HashMap<String, BomObservation>();
		for (BomObservation ob : observations) {
			map.put(ob.getFormattedDate(), ob);
		}
		return map;
	}

	/**
	 * This method takes forecast data as input parameters and generate a string
	 * in a format similar to the output format
	 * 
	 * @param cityName
	 *            the city name
	 * @param forecastDate
	 *            the date for which forecasting is done
	 * @param condition
	 *            the condition
	 * @param forecastTempData
	 *            the forecasted temp date
	 * @param forecastPressureData
	 *            the forecasted pressure data
	 * @param forecastHumidityData
	 *            the forecasted humidity data
	 * @return a string in a format similar to the output format
	 * @throws IOException
	 */
	private String generateOutput(String cityName, String forecastDate, String condition, double[] forecastTempData,
			double[] forecastPressureData, double[] forecastHumidityData) throws IOException {
		Position position = posMap.get(cityName);
		ZoneId zoneId = zoneIdsMap.get(cityName);
		ZonedDateTime dateTime = ZonedDateTime.now(java.time.ZoneId.of(zoneId.getZoneId()));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonConstants.DATETIME_ISO_8601);
		String localTime = dateTime.format(formatter);
		StringBuilder output = new StringBuilder();
		output.append(forecastDate).append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(cityName)
				.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(position.getLatitude())
				.append(CommonConstants.DELIMITER_COMMA).append(position.getLongitude())
				.append(CommonConstants.DELIMITER_COMMA).append(position.getElevation())
				.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(localTime)
				.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(condition)
				.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(CommonUtil.roundOf(forecastTempData[0]))
				.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(CommonUtil.roundOf(forecastPressureData[0]))
				.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(CommonUtil.roundOf(forecastHumidityData[0]));

		return output.toString();
	}

	/**
	 * Returns forecast data in the output format.
	 * 
	 * @throws IOException
	 */
	public String getForecastData() throws IOException {
		return forecastData.toString();

	}

	/**
	 * This method builds historical pressure data to be used for building ARIMA
	 * model. One fort night data prior to and after forecast date has been used
	 * as sample
	 * 
	 * @param forcastDate
	 *            the forecast date
	 * @param obsMap
	 *            the observations map
	 * @return the historical pressure data read
	 */
	private double[] getHistoricalPressureData(String forcastDate, Map<String, BomObservation> obsMap) {
		double[] pressureData = new double[CommonConstants.WEATHER_SAMPLE_SIZE];
		int month = DateUtil.getMonthFromDate(forcastDate);
		// if the month is after September, observation data will be for 2017,
		// else 2016
		String bomObservationDate = month > CommonConstants.VALUE_NINE ? DateUtil.subYears(forcastDate, 1)
				: forcastDate;
		int arrSize = 0;
		String pressure;
		for (int i = 14; i > 0; i--) {
			pressure = obsMap.get(DateUtil.subDays(bomObservationDate, i)).getPressure();
			pressureData[arrSize] = (CommonUtil.isNullOrEmpty(pressure)) ? 0.0 : Double.parseDouble(pressure);
			System.out.println(obsMap.get(DateUtil.subDays(bomObservationDate, i)));
			arrSize++;
		}

		for (int j = 0; j < 15; j++) {
			pressure = obsMap.get(DateUtil.addDays(bomObservationDate, j)).getPressure();
			pressureData[arrSize] = (CommonUtil.isNullOrEmpty(pressure)) ? 0.0 : Double.parseDouble(pressure);
			System.out.println(obsMap.get(DateUtil.subDays(bomObservationDate, j)));
			arrSize++;
		}

		return pressureData;
	}

	/**
	 * This method builds historical humidity data to be used for building ARIMA
	 * model. One fort night data prior to and after forecast date has been used
	 * as sample
	 * 
	 * @param forcastDate
	 *            the forecast date
	 * @param obsMap
	 *            the observations map
	 * @return the historical humidty data read
	 */
	private double[] getHistoricalHumidityData(String forcastDate, Map<String, BomObservation> obsMap) {
		double[] humidityData = new double[CommonConstants.WEATHER_SAMPLE_SIZE];
		int month = DateUtil.getMonthFromDate(forcastDate);
		// if the month is after September, observation data will be for 2017,
		// else 2016
		String bomObservationDate = month > CommonConstants.VALUE_NINE ? DateUtil.subYears(forcastDate, 1)
				: forcastDate;
		int arrSize = 0;
		String humidity;
		for (int i = 14; i > 0; i--) {
			humidity = obsMap.get(DateUtil.subDays(bomObservationDate, i)).getRelativeHumidity();
			humidityData[arrSize] = (CommonUtil.isNullOrEmpty(humidity)) ? 0.0 : Double.parseDouble(humidity);
			arrSize++;
		}

		for (int j = 0; j < 15; j++) {
			humidity = obsMap.get(DateUtil.addDays(bomObservationDate, j)).getRelativeHumidity();
			humidityData[arrSize] = (CommonUtil.isNullOrEmpty(humidity)) ? 0.0 : Double.parseDouble(humidity);
			arrSize++;
		}

		return humidityData;
	}

	/**
	 * This method builds historical temperature data (minimum and maximum
	 * temperature) to be used for building ARIMA model. One fort night data
	 * prior to and after forecast date has been used as sample.
	 * 
	 * @param forcastDate
	 *            the forecast date
	 * @param obsMap
	 *            the observations map
	 * @return the historical temperature data read
	 */
	private double[] getHistoricalTempData(String forcastDate, Map<String, BomObservation> obsMap) {
		double[] tempData = new double[CommonConstants.WEATHER_SAMPLE_SIZE];
		int month = DateUtil.getMonthFromDate(forcastDate);
		// if the month is after September, observation data will be for 2017,
		// else 2016
		String bomObservationDate = month > CommonConstants.VALUE_NINE ? DateUtil.subYears(forcastDate, 1)
				: forcastDate;
		int arrSize = 0;
		double minTemp;
		double maxTemp;
		String min;
		String max;
		for (int i = 14; i > 0; i--) {
			min = obsMap.get(DateUtil.subDays(bomObservationDate, i)).getMinTemp();
			max = obsMap.get(DateUtil.subDays(bomObservationDate, i)).getMaxTemperature();
			minTemp = CommonUtil.isNullOrEmpty(min) ? 0.0 : Double.parseDouble(min);
			maxTemp = CommonUtil.isNullOrEmpty(max) ? 0.0 : Double.parseDouble(max);
			tempData[arrSize] = (minTemp + maxTemp) / 2;
			arrSize++;
		}

		for (int j = 0; j < 15; j++) {
			min = obsMap.get(DateUtil.addDays(bomObservationDate, j)).getMinTemp();
			max = obsMap.get(DateUtil.addDays(bomObservationDate, j)).getMaxTemperature();
			minTemp = CommonUtil.isNullOrEmpty(min) ? 0.0 : Double.parseDouble(min);
			maxTemp = CommonUtil.isNullOrEmpty(max) ? 0.0 : Double.parseDouble(max);
			tempData[arrSize] = (minTemp + maxTemp) / 2;
			arrSize++;
		}

		return tempData;

	}

	/**
	 * This method builds the BOM URL.
	 * 
	 * @param bomCode
	 *            the bom code
	 * @param date
	 *            the date
	 * @return the bom URL
	 */
	private static String getBomUrl(String bomCode, String date) {
		StringBuilder bomUrl = new StringBuilder(CommonConstants.BOM_BASE_URL);
		String bomFileDate = getBomFileDate(date);
		bomUrl.append(bomFileDate).append("/text/").append(bomCode).append(".").append(bomFileDate).append(".csv");
		System.out.println(bomUrl.toString());
		return bomUrl.toString();
	}

	/**
	 * This method builds the BOM file date. The BOM site has currently date for
	 * September 2016 to September 2017 only. If the forecast month is after
	 * September, data for 2016 will be read, else data for the year 2017 will
	 * be read
	 * 
	 * @param forecastDate
	 *            the forecast date
	 * @return the bom file date as a string
	 */
	private static String getBomFileDate(String forecastDate) {
		int month = DateUtil.getMonthFromDate(forecastDate);
		String year = month > CommonConstants.VALUE_NINE ? "2016" : "2017";
		String bomFileDate = year + Integer.toString(month);
		return bomFileDate;
	}

}
