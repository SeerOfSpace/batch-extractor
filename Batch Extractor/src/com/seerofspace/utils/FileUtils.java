package com.seerofspace.utils;

import java.io.File;

public class FileUtils {
	
	public static final String FILE_SEPERATOR = "\\";
	public static final String ALT_FILE_SEPERATOR = "/";
	
	public static String getName(String path) {
		path = removeTrailingSeperators(path);
		int index1 = path.lastIndexOf(FILE_SEPERATOR);
		int index2 = path.lastIndexOf(ALT_FILE_SEPERATOR);
		int index = index1 >= index2 ? index1 : index2;
		if(index == -1) {
			return path;
		}
		return path.substring(index + 1, path.length());
	}
	
	public static String getParent(String path) {
		path = removeTrailingSeperators(path);
		int index1 = path.lastIndexOf(FILE_SEPERATOR);
		int index2 = path.lastIndexOf(ALT_FILE_SEPERATOR);
		int index = index1 >= index2 ? index1 : index2;
		if(index == -1) {
			return "";
		}
		return path.substring(0, index);
	}
	
	public static String removeTrailingSeperators(String path) {
		int index1 = path.lastIndexOf(FILE_SEPERATOR);
		int index2 = path.lastIndexOf(ALT_FILE_SEPERATOR);
		int index = index1 >= index2 ? index1 : index2;
		if(index == -1) {
			return path;
		}
		if(index + 1 == path.length()) {
			return removeTrailingSeperators(path.substring(0, index));
		}
		return path;
	}
	
	public static String getExtension(File file) {
		if(file.isDirectory()) {
			return "";
		}
		return getExtension(file.getAbsolutePath());
	}
	
	public static String getExtension(String path) {
		if(!hasExtension(path)) {
			return "";
		}
		int index = path.lastIndexOf('.');
		return path.substring(index + 1, path.length());
	}
	
	public static String removeExtension(File file) {
		if(file.isDirectory()) {
			return file.getAbsolutePath();
		}
		return removeExtension(file.getAbsolutePath());
	}
	
	public static String removeExtension(String path) {
		if(!hasExtension(path)) {
			return path;
		}
		int index = path.lastIndexOf('.');
		return path.substring(0, index);
	}
	
	public static boolean hasExtension(File file) {
		if(file.isDirectory()) {
			return false;
		}
		return hasExtension(file.getAbsolutePath());
	}
	
	public static boolean hasExtension(String path) {
		if(isPathDir(path)) {
			return false;
		}
		path = getName(path);
		int index = path.lastIndexOf('.');
		if(index == -1 || index + 1 == path.length()) {
			return false;
		}
		return true;
	}
	
	public static boolean isPathDir(String path) {
		if(path.endsWith(FILE_SEPERATOR) || path.endsWith(ALT_FILE_SEPERATOR)) {
			return true;
		}
		return false;
	}
	
	public static void deleteFolder(File file) {
		for(File entry : file.listFiles()) {
			if(entry.isFile()) {
				entry.delete();
			} else {
				deleteFolder(entry);
			}
		}
		file.delete();
	}
	
	public static File getNextFreePath(File file) {
		String name = removeExtension(file.getName());
		for(int i = 1; file.exists(); i++) {
			file = new File(getNewFileName(file, name + " " + i));
		}
		return file;
	}
	
	public static int getFileCount(File file) {
		return getFileCountRecursion(file, 0);
	}
	
	private static int getFileCountRecursion(File file, int count) {
		for(File f : file.listFiles()) { 
			if(f.isDirectory()) {
				getFileCountRecursion(f, count);
			} else {
				count++;
			}
		}
		return count;
	}
	
	public static String getNewFileName(File file, String newName) {
		return getNewFileName(file.getAbsolutePath(), newName);
	}
	
	public static String getNewFileName(String path, String newName) {
		String name = removeExtension(getName(path));
		if(name.equals(path)) {
			return newName;
		}
		int startIndex = path.lastIndexOf(name);
		int endIndex = startIndex + name.length();
		String newPath = path.substring(0, startIndex) + newName + path.substring(endIndex, path.length());
		return newPath;
	}
	
}
