package com.toy.beans;

import java.util.ArrayList;
import java.util.List;

import com.toy.constants.CommonConstants;

/**
 * A bean class holding details like City Name, IATA Code and BOM file Name
 * 
 * @author Akhil
 *
 */
public class Location {

	private String cityName;
	private String code;
	private String bomFileName;

	public Location(String cityName, String code, String bomFileName) {
		super();
		this.cityName = cityName;
		this.code = code;
		this.bomFileName = bomFileName;
	}

	public String getCityName() {
		return cityName;
	}

	public String getCode() {
		return code;
	}

	public String getBomFileName() {
		return bomFileName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setBomFileName(String bomFileName) {
		this.bomFileName = bomFileName;
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
		int i = 0;
		List<Location> locations = new ArrayList<Location>();
		while (i < lines.length) {
			String line = lines[i];
			String[] arr = line.split(CommonConstants.DELIMITER_PIPE);
			Location location = new Location(arr[0].trim(), arr[1].trim(), arr[2].trim());
			locations.add(location);
			i++;
		}
		return locations;
	}

	@Override
	public String toString() {
		return "Location [cityName=" + cityName + ", code=" + code + ", bomFileName=" + bomFileName + "]";
	}

}
