package com.toy.beans;

import java.util.ArrayList;
import java.util.List;

import com.toy.constants.CommonConstants;

/**
 * Bean class which holds position details for the city.
 * This class hold city, latitude, longitude and elevation details
 * 
 * @author Akhil
 *
 */
public class Position {
	
	private String city;
	private String latitude;
	private String longitude;
	private String elevation;
	
	public Position(String city, String latitude, String longitude, String elevation) {
		super();
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
	}
	public String getCity() {
		return city;
	}
	public String getLatitude() {
		return latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public String getElevation() {
		return elevation;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public void setElevation(String elevation) {
		this.elevation = elevation;
	}
	
	/**
	 * Loads the positions data
	 * 
	 * @param data
	 *            the data read from the positions.txt file
	 * @return the list of positions
	 */
	public static List<Position> loadPositions(String data) {
		String[] lines = data.split(CommonConstants.NEWLINE);
		int i = 0;
		List<Position> positions = new ArrayList<Position>();
		while (i < lines.length) {
			String line = lines[i];
			String[] arr = line.split(CommonConstants.DELIMITER_PIPE);
			Position position = new Position(arr[0].trim(), arr[1].trim(), arr[2].trim(), arr[3].trim());
			positions.add(position);
			i++;
		}
		return positions;
	}
	
	@Override
	public String toString() {
		return "Position [city=" + city + ", latitude=" + latitude + ", longitude=" + longitude + ", elevation="
				+ elevation + "]";
	}

}
