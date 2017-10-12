package com.toy.helper;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.toy.beans.BomObservation;
import com.toy.beans.Location;
import com.toy.beans.Position;
import com.toy.beans.ZoneId;
import com.toy.constants.CommonConstants;
import com.toy.model.ArimaPredictor;
import com.toy.util.CommonUtil;
import com.toy.util.DateUtil;
import com.toy.util.UrlUtil;

/**
 * This class is responsible for building historical values to be supplied to
 * the ARIMA model. It builds - temperature, pressure and relative humidity
 * information. The historical values built will be supplied to ARIMA model for
 * prediction. The historical values are downloaded from BOM website.
 * 
 * This class is also responsible for generating the predicted results in a
 * predefined format. The format is as below.
 * 
 * Forecast_Date|Location|Latitude,Longitude,
 * Elevation|LocalTime|Condition|Temperature|Pressure|Relative_Humidity
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

	private static final Logger logger = Logger.getLogger(WeatherDataGenerator.class);

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
	 * This method predict temperature, pressure and humidity data for a city
	 * based on historical weather data. Historical data is being pulled from
	 * BOM site. For prediction, it make use of a popular forecasting model
	 * called ARIMA.
	 * 
	 * The forecast results will be stored in forecastData variable
	 * 
	 * @throws IOException
	 */
	public void generateForcastData() throws IOException {
		forecastData = new StringBuilder();
		// Populate BOM Observation data; the map will 3 months of data - for
		// the forecasting month, one month prior to forecast start
		// date, one month after forecast start date
		Map<String, BomObservation> obsMap = getBomObservationsAsMap(location.getBomCode(), forecastStartDate);
		String city = location.getCityName();
		for (int i = 0; i < numDays; i++) {
			String forcastDate = DateUtil.addDays(forecastStartDate, i);
			logger.info("Prediting weather for " + forcastDate);
			double[] tempData = getHistoricalTempData(forcastDate, obsMap);
			double[] humidityData = getHistoricalHumidityData(forcastDate, obsMap);
			double[] pressureData = getHistoricalPressureData(forcastDate, obsMap);
			double[] forecastTempData = ArimaPredictor.forcast(tempData, CommonConstants.FORCAST_SIZE_ONE);
			double[] forecastHumidityData = ArimaPredictor.forcast(humidityData, CommonConstants.FORCAST_SIZE_ONE);
			double[] forecastPressureData = ArimaPredictor.forcast(pressureData, CommonConstants.FORCAST_SIZE_ONE);
			double maxSunShineHours = getMaxSunshineHours(forcastDate, obsMap);
			String condition = predictCondition(forecastTempData[0], forecastHumidityData[0], maxSunShineHours);
			String out = generateOutput(city, forcastDate, condition, forecastTempData, forecastPressureData,
					forecastHumidityData);
			forecastData.append(out);
			forecastData.append(CommonConstants.NEWLINE);
		}
	}

	/**
	 * Predict Weather condition based on Temperature, Humidity and sunshine
	 * hours.
	 * 
	 * @param temp
	 *            the temperature
	 * @param relativeHumidity
	 *            the relative humidity
	 * @param sunShineHours
	 *            the sun shine hours
	 * @return the condition (SUNNY, SNOW, RAIN, CLOUDY etc)
	 */
	private String predictCondition(double temp, double relativeHumidity, double sunShineHours) {

		if (temp < 0) {
			return "SNOW";
		}
		if (relativeHumidity > 80 && sunShineHours < 4) {
			return "RAIN";
		}

		if (sunShineHours > 5) {
			return "SUNNY";
		}
		return "CLOUDY";
	}

	/**
	 * This method reads/downloads historical weather data for a city. The
	 * weather data will be downloaded from BOM website. It will download three
	 * months of data - data for the forecast month, for one month prior to
	 * forecast month and one month after forecast month
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
	 * This method takes weather prediction results as input parameters and
	 * generate a string in a format similar to the output format
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
			arrSize++;
		}

		for (int j = 0; j < 15; j++) {
			pressure = obsMap.get(DateUtil.addDays(bomObservationDate, j)).getPressure();
			pressureData[arrSize] = (CommonUtil.isNullOrEmpty(pressure)) ? 0.0 : Double.parseDouble(pressure);
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
	 * @return the historical humidity data read
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
	 * The methods finds out the maximum sunshine by using data for one
	 * fortnight (before and after forecast date)
	 * 
	 * @param forcastDate
	 *            the forecast date
	 * @param obsMap
	 *            the observations map
	 * @return the historical pressure data read
	 */
	private double getMaxSunshineHours(String forcastDate, Map<String, BomObservation> obsMap) {
		int month = DateUtil.getMonthFromDate(forcastDate);
		// if the month is after September, observation data will be for 2017,
		// else 2016
		String bomObservationDate = month > CommonConstants.VALUE_NINE ? DateUtil.subYears(forcastDate, 1)
				: forcastDate;

		double maxValue = 0.0;
		String sunshineHours;
		double hours;
		for (int i = 14; i > 0; i--) {
			sunshineHours = obsMap.get(DateUtil.subDays(bomObservationDate, i)).getSunshine();
			hours = (CommonUtil.isNullOrEmpty(sunshineHours)) ? 0.0 : Double.parseDouble(sunshineHours);
			maxValue = hours > maxValue ? hours : maxValue;
		}

		for (int j = 0; j < 15; j++) {
			sunshineHours = obsMap.get(DateUtil.addDays(bomObservationDate, j)).getSunshine();
			hours = (CommonUtil.isNullOrEmpty(sunshineHours)) ? 0 : Double.parseDouble(sunshineHours);
			maxValue = hours > maxValue ? hours : maxValue;
		}

		return maxValue;
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
		logger.info("BOM URL:- " + bomUrl.toString());
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
		String bomFileDate = year + formatBomMonth(month);
		return bomFileDate;
	}

	/**
	 * This method format the month to BOM month format. It basically prepend a
	 * zero if the month is before 10
	 * 
	 * @param month
	 *            the month
	 * @return formatted month
	 */
	private static String formatBomMonth(int month) {
		String formattedMonth = Integer.toString(month);
		if (month < 10) {
			formattedMonth = "0" + formattedMonth;
		}
		return formattedMonth;
	}

}
