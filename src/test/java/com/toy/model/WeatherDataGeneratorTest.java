package com.toy.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.toy.beans.Location;
import com.toy.beans.Position;
import com.toy.beans.ZoneId;

public class WeatherDataGeneratorTest {

	public Location location;
	public String expectedForecastData;

	@Before
	public void setUp() throws Exception {
		String locationData = "SYDNEY|SYD|IDCJDW2124";
		String positionData = "SYDNEY|-33.86|151.12|58";
		String zoneData = "SYDNEY|Australia/Sydney";
		int numDays = 1;
		String forecastStartDate = "2017-12-12";
		List<Location> locations = Location.loadLocations(locationData);
		List<Position> positions = Position.loadPositions(positionData);
		List<ZoneId> zoneIds = ZoneId.load(zoneData);
		Map<String, Position> posMap = new HashMap<String, Position>();
		posMap.put(positions.get(0).getCity(), positions.get(0));
		Map<String, ZoneId> zoneIdsMap = new HashMap<String, ZoneId>();
		zoneIdsMap.put(zoneIds.get(0).getCity(), zoneIds.get(0));
		WeatherDataGenerator.forecastStartDate = forecastStartDate;
		WeatherDataGenerator.numDays = numDays;
		WeatherDataGenerator.posMap = posMap;
		WeatherDataGenerator.zoneIdsMap = zoneIdsMap;
		location = locations.get(0);
		expectedForecastData = "^2017-12-12|SYDNEY|-33.86,151.12,58|.*||24.44|1014.89|57.0$";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenerateForecastData() throws IOException {
		WeatherDataGenerator wdg = new WeatherDataGenerator(location);
		wdg.generateForcastData();
		String forecastData = wdg.getForecastData();
		System.out.println(forecastData);
		String actualData = "2017-12-12|SYDNEY|-33.86,151.12,58|2017-10-11T04:11:10+1100||24.44|1014.89|57.0";
		Pattern pattern = Pattern.compile(expectedForecastData);
		assertTrue(pattern.matcher(actualData).matches());
	}

}
