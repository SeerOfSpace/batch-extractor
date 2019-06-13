package core;

import java.io.File;

public class CustomFileUtils {
	
	public static String getName(String s) {
		int index = s.lastIndexOf(File.separator);
		if(index == -1) {
			index = s.lastIndexOf('/');
			if(index == -1) {
				return s;
			}
		}
		return s.substring(index + 1, s.length());
	}
	
	public static String getExtension(String s) {
		return s.substring(s.lastIndexOf('.') + 1, s.length());
	}
	
	public static String removeExtension(String s) {
		return s.substring(0, s.lastIndexOf('.'));
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
		String path = file.getAbsolutePath();
		for(int i = 1; file.exists(); i++) {
			file = new File(path + " " + i);
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
	
}
