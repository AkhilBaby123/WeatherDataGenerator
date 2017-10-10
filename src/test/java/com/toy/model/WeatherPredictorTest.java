package com.toy.model;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeatherPredictorTest {

	double[] histValues;
	int forecastSize;

	@Before
	public void setUp() throws Exception {
		histValues = new double[] { 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0,
				22.0, 22.0, 22.0, 22.0 };
		forecastSize = 1;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testArimaForcast() {
		double[] expected = new double[] { 22.0 };
		double[] result = WeatherPredictor.forcast(histValues, forecastSize);
		System.out.println(expected[0]);
		assertTrue(Arrays.equals(expected, result));
	}

}
