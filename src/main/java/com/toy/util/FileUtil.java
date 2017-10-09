package com.toy.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.toy.constants.CommonConstants;

public class FileUtil {

	/**
	 * Read data from the file name specified.
	 * 
	 * @param fileName
	 *            the absolute path of the file
	 * @return contents from the file.
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException {

		if (fileName == null || fileName.isEmpty()) {
			System.out.println("File Name is NULL/Empty..");
			return null;
		}

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
		} catch (Exception e) {
			e.printStackTrace();
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
			System.out.println("File Name/Content to write is NULL/Empty..");
			return;
		}
		FileWriter writer = null;
		BufferedWriter bw = null;
		try{
			writer = new FileWriter(new File(fileName), append);
			bw = new BufferedWriter(writer);
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bw!=null){
				bw.close();
			}
			if(writer!=null){
				writer.close();
			}
		}
	}
	
	public static boolean isFileExist(String fileName){
		File file = new File(fileName);
		return file.exists();
	}
	
	public static void deleteFile(String fileName ) { 
		File file = new File(fileName);
		file.delete();
	}

}
