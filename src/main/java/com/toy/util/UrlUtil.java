package com.toy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.toy.constants.CommonConstants;

public class UrlUtil {

	public static String readUrl(String url) throws IOException {
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
