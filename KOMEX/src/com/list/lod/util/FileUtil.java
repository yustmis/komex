package com.list.lod.util;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

public class FileUtil {
	public static void fileCopy(String from) {
		//File orgFile = new File(orgFilePath + File.separator + fileName); // File
																			// upload
																			// Folder
		
		String dirPath = ResourceBundle.fileBundle.getString("output_file_path");
		String fromFullPath = dirPath + File.separator + from+".ttl";
		
		String targetDir = ResourceBundle.fileBundle.getString("ontobase_dir");
		String toFullPath = targetDir + File.separator + from+".ttl";
		
		try {
			Files.copy(new File(fromFullPath), new File(toFullPath));
		} catch (IOException e1) {
			System.out.println("File Copy Error ");
			e1.printStackTrace();
		}
	}
}
