package com.toy.model;

import com.toy.constants.ModelConstants;
import com.workday.insights.timeseries.arima.Arima;
import com.workday.insights.timeseries.arima.struct.ArimaParams;
import com.workday.insights.timeseries.arima.struct.ForecastResult;

public class WeatherPredictor {

	public static double[] forcast(double[] histValues, int forecastSize) {
		int p = ModelConstants.MODEL_PARAM_p;
		int d = ModelConstants.MODEL_PARAM_d;
		int q = ModelConstants.MODEL_PARAM_q;
		int P = ModelConstants.MODEL_PARAM_P;
		int D = ModelConstants.MODEL_PARAM_D;
		int Q = ModelConstants.MODEL_PARAM_Q;
		int m = ModelConstants.MODEL_PARAM_m;
		ArimaParams params = new ArimaParams(p, d, q, P, D, Q, m);
		// Obtain forecast result. The structure contains forecasted values and
		// performance metric etc.
		ForecastResult forecastResult = Arima.forecast_arima(histValues, forecastSize, params);
		// Read forecast values
		double[] forecastData = forecastResult.getForecast();
		return forecastData;
	}

}
