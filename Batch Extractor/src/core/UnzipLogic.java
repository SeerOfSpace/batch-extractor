package core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnzipLogic {
	
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
	
	public static void unzip(File source) throws IOException {
		String name = removeExtension(source.getName());
		File parent = new File(source.getParent() + File.separator + name);
		unzip(source, parent);
	}
	
	public static void unzip(File source, File parent) throws IOException {
		parent.mkdirs();
		ZipFile zipFile = new ZipFile(source);
		Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
		while(zipEnum.hasMoreElements()) {
			ZipEntry entry = zipEnum.nextElement();
			File dest = new File(parent, entry.getName());
			dest.getParentFile().mkdirs();
			if(!entry.isDirectory()) {
				BufferedInputStream inputStream = new BufferedInputStream(zipFile.getInputStream(entry));
				BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(dest));
				byte[] buffer = new byte[1024];
				int len;
				while((len = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, len);
				}
				inputStream.close();
				outputStream.close();
			}
		}
		zipFile.close();
	}
	
}
