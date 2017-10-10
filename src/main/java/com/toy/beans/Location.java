package com.toy.beans;

import java.util.ArrayList;
import java.util.List;

import com.toy.constants.CommonConstants;

/**
 * A bean class holding details like City Name, IATA Code and BOM Code
 * 
 * @author Akhil
 *
 */
public class Location {

	private String cityName;
	private String code;
	private String bomCode;

	public Location(String cityName, String code, String bomCode) {
		super();
		this.cityName = cityName;
		this.code = code;
		this.bomCode = bomCode;
	}

	public String getCityName() {
		return cityName;
	}

	public String getCode() {
		return code;
	}

	public String getBomCode() {
		return bomCode;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setBomCode(String bomCode) {
		this.bomCode = bomCode;
	}

	/**
	 * Loads the locations data
	 * 
	 * @param data
	 *            the data read from the locations.txt file
	 * @return the list of locations
	 */
	public static List<Location> loadLocations(String data) {
		String[] lines = data.split(CommonConstants.NEWLINE);
		List<Location> locations = new ArrayList<Location>();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			String[] arr = line.split(CommonConstants.DELIMITER_PIPE);
			Location location = new Location(arr[0].trim(), arr[1].trim(), arr[2].trim());
			locations.add(location);
		}
		return locations;
	}

	@Override
	public String toString() {
		return "Location [cityName=" + cityName + ", code=" + code + ", bomFileName=" + bomCode + "]";
	}

}
