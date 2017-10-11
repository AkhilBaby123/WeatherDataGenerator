package com.toy.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.toy.constants.CommonConstants;

public class FileUtil {
	private static final Logger logger = Logger.getLogger(FileUtil.class);

	/**
	 * Read data from the file specified.
	 * 
	 * @param fileName
	 *            the absolute path of the file
	 * @return contents from the file.
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException {
		if (CommonUtil.isNullOrEmpty(fileName)) {
			logger.error("File Name cannot be NULL or empty..");
			return null;
		}

		logger.info("Reading data from -> " + fileName);

		FileReader reader = null;
		BufferedReader br = null;
		String st = null;
		StringBuilder content = new StringBuilder();
		try {
			reader = new FileReader(new File(fileName));
			br = new BufferedReader(reader);
			while ((st = br.readLine()) != null) {
				content.append(st);
				content.append(CommonConstants.NEWLINE);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (br != null) {
				br.close();
			}
		}
		return content.toString();
	}

	public static void writeToFile(String fileName, String content, boolean append) throws IOException {
		if (CommonUtil.isNullOrEmpty(fileName) || CommonUtil.isNullOrEmpty(content)) {
			logger.error("File Name/Content to write is NULL/Empty..");
			return;
		}
		FileWriter writer = null;
		BufferedWriter bw = null;
		try {
			writer = new FileWriter(new File(fileName), append);
			bw = new BufferedWriter(writer);
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				bw.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	/**
	 * Check if the file specified by the fileName argument passed exists
	 * 
	 * @param fileName
	 *            the file name
	 * @return true if the file exists
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * Delete the file if it already exist
	 * 
	 * @param fileName
	 *            the file name
	 */
	public static void deleteFile(String fileName) {
		if (CommonUtil.isNullOrEmpty(fileName)) {
			logger.error("Input Argument (File Name) cannot be NULL/empty");
			return;
		}
		File file = new File(fileName);
		file.delete();
	}

}
