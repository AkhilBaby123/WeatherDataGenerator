package com.toy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import com.toy.constants.CommonConstants;

public class UrlUtil {
	private static final Logger logger = Logger.getLogger(UrlUtil.class);

	/**
	 * Read data from a URL supplied. The read data will be returned to the
	 * caller
	 * 
	 * @param url
	 *            the URL from the data needs to be read
	 * @return the data read
	 * @throws IOException
	 */
	public static String readUrl(String url) throws IOException {

		logger.info("Reading data from url -> " + url);

		URL webUrl = null;
		URLConnection connection = null;
		BufferedReader br = null;
		StringBuilder response = new StringBuilder();

		try {
			webUrl = new URL(url);
			connection = webUrl.openConnection();
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				response.append(line);
				response.append(CommonConstants.NEWLINE);

			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
		return response.toString();
	}

}
