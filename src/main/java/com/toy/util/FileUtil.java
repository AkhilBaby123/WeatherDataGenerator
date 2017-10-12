package com.toy.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
		BufferedReader br = null;
		String st = null;
		StringBuilder content = new StringBuilder();
		ClassLoader classLoader = FileUtil.class.getClassLoader();
		InputStream in = classLoader.getResourceAsStream(fileName);
		if (in == null) {
			logger.error("Cannot find the file " + fileName);
			return null;
		}
		logger.info("Reading data from -> " + fileName);
		try {
			br = new BufferedReader(new InputStreamReader(in));
			while ((st = br.readLine()) != null) {
				content.append(st);
				content.append(CommonConstants.NEWLINE);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			if (br != null) {
				br.close();
			}
			in.close();
		}
		return content.toString();
	}

	/**
	 * Write the contents passed to the file name specified
	 * 
	 * @param fileName
	 *            the file name
	 * @param content
	 *            the content
	 * @param append
	 *            arguments which specifies whether to append data to the file
	 * @throws IOException
	 */
	public static void writeToFile(String fileName, String content, boolean append) throws IOException {
		if (CommonUtil.isNullOrEmpty(fileName) || CommonUtil.isNullOrEmpty(content)) {
			logger.error("File Name/Content to write is NULL/Empty..");
			return;
		}
		FileWriter writer = null;
		BufferedWriter bw = null;
		logger.info("Writing data to -> " + fileName);
		try {
			writer = new FileWriter(new File(fileName), append);
			bw = new BufferedWriter(writer);
			bw.write(content);
		} catch (IOException e) {
			logger.error(e.getMessage());
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
			logger.error("Input Argument (File Name) cannot be NULL/empty..<FileUtil.DeleteFile>");
			return;
		}
		File file = new File(fileName);
		file.delete();
	}

}
