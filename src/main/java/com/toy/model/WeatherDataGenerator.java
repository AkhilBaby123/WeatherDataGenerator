package com.toy.model;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.toy.beans.BomObservation;
import com.toy.beans.Location;
import com.toy.beans.Position;
import com.toy.beans.ZoneId;
import com.toy.constants.CommonConstants;
import com.toy.util.CommonUtil;
import com.toy.util.DateUtil;
import com.toy.util.FileUtil;
import com.toy.util.UrlUtil;

public class WeatherDataGenerator {

	public static void generateWeatherData(List<Location> locations, Map<String, String> argsMap) throws IOException {
		if (locations == null || argsMap == null) {
			System.out.println("Input arguments cannot be null <WeatherDataGenerator.generateWeatherData>");
			System.exit(0);
		}
		String baseForecastDate = argsMap.get(CommonConstants.FORCAST_DATE);
		int numDays = Integer.parseInt(argsMap.get(CommonConstants.NUM_DAYS));
		String bomFileDate = baseForecastDate;
		if (DateUtil.getMonthFromDate(baseForecastDate) > CommonConstants.VALUE_NINE) {
			bomFileDate = DateUtil.subYears(argsMap.get(CommonConstants.FORCAST_DATE), 1);
		}
		Iterator<Location> it = locations.iterator();
		StringBuilder builder = new StringBuilder();
		StringBuilder output = new StringBuilder();
		String positionData = FileUtil.readFile(CommonConstants.POSITIONS_FILE);
		List<Position> positions = Position.loadPositions(positionData);
		Map<String, Position> posMap = getPositionsAsMap(positions);
		String zoneData = FileUtil.readFile(CommonConstants.FILE_NAME_ZONE_IDS);
		List<ZoneId> zoneIds = ZoneId.load(zoneData);
		Map<String, ZoneId> zoneMap = getZoneMap(zoneIds);
		String fileName = argsMap.get(CommonConstants.OUTPUTFILEPATH);
		clearOutput(fileName);
		while (it.hasNext()) {
			Location location = it.next();
			builder.append(UrlUtil.readUrl(getBomUrl(location.getBomFileName(), bomFileDate)));
			builder.append(UrlUtil.readUrl(getBomUrl(location.getBomFileName(), DateUtil.subMonths(bomFileDate, 1))));
			builder.append(UrlUtil.readUrl(getBomUrl(location.getBomFileName(), DateUtil.addMonths(bomFileDate, 1))));
			List<BomObservation> observations = BomObservation.load(builder.toString());
			Map<String, BomObservation> obsMap = getObservationsAsMap(observations);
			builder.setLength(0);
			String city = location.getCityName();
			Position position = posMap.get(city);
			ZoneId zoneId = zoneMap.get(city);

			ZonedDateTime dateTime = ZonedDateTime.now(java.time.ZoneId.of(zoneId.getZoneId()));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonConstants.DATETIME_ISO_8601);
			String localTime = dateTime.format(formatter);
			for (int i = 0; i < numDays; i++) {
				String forcastDate = DateUtil.addDays(baseForecastDate, i);
				double[] tempData = getTempData(forcastDate, obsMap);
				double[] humidityData = getHumidityData(forcastDate, obsMap);
				double[] pressureData = getPressureData(forcastDate, obsMap);
				double[] forcastTempData = WeatherPredictor.forcast(tempData, CommonConstants.FORCAST_SIZE_ONE);
				double[] forcastHumidityData = WeatherPredictor.forcast(humidityData, CommonConstants.FORCAST_SIZE_ONE);
				double[] forcastPressureData = WeatherPredictor.forcast(pressureData, CommonConstants.FORCAST_SIZE_ONE);
				// TODO -- populate condition
				String condition = "";

				output.append(forcastDate).append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(city)
						.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(position.getLatitude())
						.append(CommonConstants.DELIMITER_COMMA).append(position.getLongitude())
						.append(CommonConstants.DELIMITER_COMMA).append(position.getElevation())
						.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(localTime)
						.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(condition)
						.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(roundOf(forcastTempData[0]))
						.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(roundOf(forcastPressureData[0]))
						.append(CommonConstants.DELIMITER_PIPE_NON_ESCAPE).append(roundOf(forcastHumidityData[0]));
				output.append(CommonConstants.NEWLINE);
				//System.out.println(output.toString());
			}
            FileUtil.writeToFile(fileName, output.toString(), true);
            output.setLength(0);
		}
	}

	

	private static double[] getPressureData(String forcastDate, Map<String, BomObservation> obsMap) {
		double[] pressureData = new double[CommonConstants.WEATHER_SAMPLE_SIZE];
		String bomFileDate = DateUtil.subYears(forcastDate, 1);
		int arrSize = 0;
		String pressure;
		for (int i = 14; i > 0; i--) {
			pressure = obsMap.get(DateUtil.subDays(bomFileDate, i)).getPressure();
			pressureData[arrSize] = (CommonUtil.isNullOrEmpty(pressure)) ? 0.0 : Double.parseDouble(pressure);
			System.out.println(obsMap.get(DateUtil.subDays(bomFileDate, i)));
			arrSize++;
		}

		for (int j = 0; j < 15; j++) {
			pressure = obsMap.get(DateUtil.addDays(bomFileDate, j)).getPressure();
			pressureData[arrSize] = (CommonUtil.isNullOrEmpty(pressure)) ? 0.0 : Double.parseDouble(pressure);
			System.out.println(obsMap.get(DateUtil.subDays(bomFileDate, j)));
			arrSize++;
		}

		return pressureData;
	}

	private static double[] getHumidityData(String forcastDate, Map<String, BomObservation> obsMap) {
		double[] humidityData = new double[CommonConstants.WEATHER_SAMPLE_SIZE];
		String bomFileDate = DateUtil.subYears(forcastDate, 1);
		int arrSize = 0;
		String humidity;
		for (int i = 14; i > 0; i--) {
			humidity = obsMap.get(DateUtil.subDays(bomFileDate, i)).getRelativeHumidity();
			humidityData[arrSize] = (CommonUtil.isNullOrEmpty(humidity)) ? 0.0 : Double.parseDouble(humidity);
			arrSize++;
		}

		for (int j = 0; j < 15; j++) {
			humidity = obsMap.get(DateUtil.addDays(bomFileDate, j)).getRelativeHumidity();
			humidityData[arrSize] = (CommonUtil.isNullOrEmpty(humidity)) ? 0.0 : Double.parseDouble(humidity);
			arrSize++;
		}

		return humidityData;
	}

	private static double[] getTempData(String forcastDate, Map<String, BomObservation> obsMap) {
		double[] tempData = new double[CommonConstants.WEATHER_SAMPLE_SIZE];
		String bomFileDate = DateUtil.subYears(forcastDate, 1);
		int arrSize = 0;
		double minTemp;
		double maxTemp;
		String min;
		String max;
		for (int i = 14; i > 0; i--) {
			min = obsMap.get(DateUtil.subDays(bomFileDate, i)).getMinTemp();
			max = obsMap.get(DateUtil.subDays(bomFileDate, i)).getMaxTemperature();
			minTemp = CommonUtil.isNullOrEmpty(min) ? 0.0 : Double.parseDouble(min);
			maxTemp = CommonUtil.isNullOrEmpty(max) ? 0.0 : Double.parseDouble(max);
			tempData[arrSize] = (minTemp + maxTemp) / 2;
			arrSize++;
		}

		for (int j = 0; j < 15; j++) {
			min = obsMap.get(DateUtil.addDays(bomFileDate, j)).getMinTemp();
			max = obsMap.get(DateUtil.addDays(bomFileDate, j)).getMaxTemperature();
			minTemp = CommonUtil.isNullOrEmpty(min) ? 0.0 : Double.parseDouble(min);
			maxTemp = CommonUtil.isNullOrEmpty(max) ? 0.0 : Double.parseDouble(max);
			tempData[arrSize] = (minTemp + maxTemp) / 2;
			arrSize++;
		}

		return tempData;

	}

	private static String getBomUrl(String bomFileName, String date) {
		StringBuilder bomUrl = new StringBuilder(CommonConstants.BOM_BASE_URL);
		String d = DateUtil.formatDate(date, CommonConstants.DATE_FROMAT_YYYYMM);
		bomUrl.append(d).append("/text/").append(bomFileName).append(".").append(d).append(".csv");
		System.out.println(bomUrl.toString());
		return bomUrl.toString();
	}

	private static Map<String, BomObservation> getObservationsAsMap(List<BomObservation> observations) {
		Map<String, BomObservation> map = new HashMap<String, BomObservation>();
		Iterator<BomObservation> it = observations.iterator();
		while (it.hasNext()) {
			BomObservation ob = it.next();
			map.put(ob.getFormattedDate(), ob);
		}
		return map;
	}

	private static Map<String, ZoneId> getZoneMap(List<ZoneId> zoneIds) {
		Map<String, ZoneId> map = new HashMap<String, ZoneId>();
		Iterator<ZoneId> it = zoneIds.iterator();
		while (it.hasNext()) {
			ZoneId zoneId = it.next();
			map.put(zoneId.getCity(), zoneId);
		}
		return map;
	}

	private static Map<String, Position> getPositionsAsMap(List<Position> positions) {
		Map<String, Position> map = new HashMap<String, Position>();
		Iterator<Position> it = positions.iterator();
		while (it.hasNext()) {
			Position pos = it.next();
			map.put(pos.getCity(), pos);
		}
		return map;
	}

	private static double roundOf(double value) {
		double finalValue = Math.round(value * 100.0) / 100.0;
		return finalValue;
	}
	
	private static void clearOutput(String fileName) {
		if(FileUtil.isFileExist(fileName)){
			FileUtil.deleteFile(fileName);
		}
	}

}
