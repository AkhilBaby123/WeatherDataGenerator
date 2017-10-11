package com.toy.model;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ArimaPredictorTest {

	double[] histValues;
	int forecastSize;

	@Before
	public void setUp() throws Exception {
		histValues = new double[] { 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0, 22.0,
				22.0, 22.0, 22.0, 22.0 };
		forecastSize = 1;
	}

	@Test
	public void testArimaForcast() {
		double[] expected = new double[] { 22.0 };
		double[] result = ArimaPredictor.forcast(histValues, forecastSize);
		System.out.println(expected[0]);
		assertTrue(Arrays.equals(expected, result));
	}

}
