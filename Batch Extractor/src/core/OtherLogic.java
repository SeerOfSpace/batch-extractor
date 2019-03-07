package core;

import java.io.File;
import java.io.IOException;

public class OtherLogic {
	
	public static void rename(File file) {
		for(File entry : file.listFiles()) {
			String name = entry.getName();
			int index = name.indexOf('_', 1 + name.indexOf('_', 1 + name.indexOf('_', 1 + name.indexOf('_'))));
			String newPath = entry.getParent() + File.separator + name.substring(index + 1, name.length());
			File newFile = new File(newPath);
			entry.renameTo(newFile);
		}
	}
	
	public static File unzipMainZip(File file) {
		File temp = null;
		try {
			temp = new File(UnzipLogic.removeExtension(file.getAbsolutePath()));
			UnzipLogic.unzip(file, temp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
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
	
}
