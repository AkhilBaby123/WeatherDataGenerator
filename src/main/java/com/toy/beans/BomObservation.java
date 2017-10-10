package com.toy.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.toy.constants.CommonConstants;
import com.toy.util.DateUtil;

/**
 * A class representing BOM Observation data read from BOM site
 * 
 * @author Akhil
 *
 */
public class BomObservation {

	private String date;// position 1
	private String minTemp;// position 2
	private String maxTemperature;// position 3
	private String sunshine;// position 6

	private String relativeHumidity;// position 11
	private String cloudAmount; // position 12
	private String windSpeed;// position 14
	private String pressure;// position 15
	private String formattedDate; // the date value in yyyy-MM-dd format

	public BomObservation(String date, String minTemp, String maxTemperature, String sunshine, String windSpeed,
			String relativeHumidity, String cloudAmount, String pressure) {
		super();
		this.date = date;
		this.minTemp = minTemp;
		this.maxTemperature = maxTemperature;
		this.sunshine = sunshine;
		this.windSpeed = windSpeed;
		this.relativeHumidity = relativeHumidity;
		this.cloudAmount = cloudAmount;
		this.pressure = pressure;
		formatDate(date);
	}

	public String getDate() {
		return date;
	}

	public String getMinTemp() {
		return minTemp;
	}

	public String getMaxTemperature() {
		return maxTemperature;
	}

	public String getSunshine() {
		return sunshine;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public String getRelativeHumidity() {
		return relativeHumidity;
	}

	public String getCloudAmount() {
		return cloudAmount;
	}

	public String getPressure() {
		return pressure;
	}

	public String getFormattedDate() {
		return formattedDate;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setMinTemp(String minTemp) {
		this.minTemp = minTemp;
	}

	public void setMaxTemperature(String maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public void setSunshine(String sunshine) {
		this.sunshine = sunshine;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public void setRelativeHumidity(String relativeHumidity) {
		this.relativeHumidity = relativeHumidity;
	}

	public void setCloudAmount(String cloudAmount) {
		this.cloudAmount = cloudAmount;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	/**
	 * This method creates BOMObservation objects representing BOM data read.
	 * 
	 * @param data
	 *            the data read from BOM observation file
	 * @return the list of BOMObservations for the data read
	 */
	public static List<BomObservation> load(String data) {
		String[] lines = data.split(CommonConstants.NEWLINE);
		int i = 0;
		List<BomObservation> observations = new ArrayList<BomObservation>();
		Pattern pattern = Pattern.compile(CommonConstants.BOM_DATA_PATTERN);
		while (i < lines.length) {
			String line = lines[i];
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				String[] arr = line.split(CommonConstants.DELIMITER_COMMA);
				BomObservation observation = new BomObservation(arr[1], arr[2], arr[3], arr[6], arr[14], arr[11],
						arr[12], arr[15]);
				observations.add(observation);
			}
			i++;
		}
		return observations;
	}

	@Override
	public String toString() {
		return "BomObservation [date=" + date + ", minTemp=" + minTemp + ", maxTemperature=" + maxTemperature
				+ ", sunshine=" + sunshine + ", windSpeed=" + windSpeed + ", relativeHumidity=" + relativeHumidity
				+ ", cloudAmount=" + cloudAmount + ", pressure=" + pressure + " ]";
	}

	private void formatDate(String date) {
		String formattedDate = DateUtil.formatDate(date, CommonConstants.DATE_FORMAT_YYYY_MM_DD);
		setFormattedDate(formattedDate);

	}

	private void setFormattedDate(String date) {
		this.formattedDate = date;

	}

}
