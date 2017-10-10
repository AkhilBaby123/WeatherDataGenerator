package com.toy.model;

import org.apache.log4j.Logger;

import com.toy.constants.ModelConstants;
import com.workday.insights.timeseries.arima.Arima;
import com.workday.insights.timeseries.arima.struct.ArimaParams;
import com.workday.insights.timeseries.arima.struct.ForecastResult;

/**
 * Class which uses ARIMA model to forecast data.
 * 
 * @author Akhil
 *
 */
public class WeatherPredictor {

	private static final Logger logger = Logger.getLogger(WeatherPredictor.class);

	/**
	 * This method predict future data based on historical data supplied. For
	 * forecasting, ARIMA model has been used. Forecasting will be done for up
	 * to the value specified as forecast size argument
	 * 
	 * @param histValues
	 *            the historical value to be used for prediction
	 * @param forecastSize
	 *            the number of days for which we need to predict
	 * @return the forecasted data
	 */
	public static double[] forcast(double[] histValues, int forecastSize) {
		int p = ModelConstants.MODEL_PARAM_p;
		int d = ModelConstants.MODEL_PARAM_d;
		int q = ModelConstants.MODEL_PARAM_q;
		int P = ModelConstants.MODEL_PARAM_P;
		int D = ModelConstants.MODEL_PARAM_D;
		int Q = ModelConstants.MODEL_PARAM_Q;
		int m = ModelConstants.MODEL_PARAM_m;
		ArimaParams params = new ArimaParams(p, d, q, P, D, Q, m);
		// Obtain forecast result.
		logger.info("Calling ARIMA model for forecasting..");
		ForecastResult forecastResult = Arima.forecast_arima(histValues, forecastSize, params);
		// Read forecast values
		double[] forecastData = forecastResult.getForecast();
		return forecastData;
	}

}
