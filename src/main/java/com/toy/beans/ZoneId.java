package com.toy.beans;

import java.util.ArrayList;
import java.util.List;

import com.toy.constants.CommonConstants;

public class ZoneId {
	private String city;
	private String zoneId;
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public ZoneId(String city, String zoneId) {
		super();
		this.city = city;
		this.zoneId = zoneId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	
	public static List<ZoneId> load(String data){
		String[] lines = data.split(CommonConstants.NEWLINE);
		int i = 0;
		List<ZoneId> zoneIds = new ArrayList<ZoneId>();
		while (i < lines.length) {
			String line = lines[i];
			String[] arr = line.split(CommonConstants.DELIMITER_PIPE);
			ZoneId zoneId = new ZoneId(arr[0], arr[1]);
			zoneIds.add(zoneId);
			i++;
		}
		return zoneIds;
	}
	
	@Override
	public String toString() {
		return "TimeZones [city=" + city + ", zoneId=" + zoneId + "]";
	}

}
