package core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.*;

public class Main {
	
	public static void main(String[] args) {
		if(args.length == 0) {
			System.out.println("Error: Must have a single argument");
		} else if(args.length > 1) {
			System.out.println("Error: Too many arguments");
		} else {
			File source = new File(args[0]);
			try {
				if(source.isDirectory()) {
					unzipFolderJava(source);
				} else if(source.isFile()) {
					unzipJava(source);
				} else {
					System.out.println("Error: Invalid path");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
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
	
	public static void unzipFolderJava(File dir) throws IOException {
		File parent = new File(dir.getAbsoluteFile().getParent() + File.separator + dir.getName() + " Unzipped");
		unzipFolderJava(dir, parent);
	}
	
	public static void unzipFolderJava(File dir, File parent) throws IOException {
		parent.mkdirs();
		for(File file : dir.listFiles()) {
			if(file.isDirectory()) {
				unzipFolderJava(file, parent);
			} else if(getExtension(file.getName()).equals("zip") && file.getName().toLowerCase().contains("moss")) {
				File zipParent = new File(parent.getAbsolutePath() + File.separator + removeExtension(file.getName()));
				unzipJava(file, zipParent);
			}
		}
	}
	
	public static void unzipJava(File source) throws IOException {
		String name = removeExtension(source.getName());
		File parent = new File(source.getParent() + File.separator + name);
		unzipJava(source, parent);
	}
	
	public static void unzipJava(File source, File parent) throws IOException {
		parent.mkdirs();
		ZipFile zipFile = new ZipFile(source);
		Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
		while(zipEnum.hasMoreElements()) {
			ZipEntry entry = zipEnum.nextElement();
			if(!entry.isDirectory() && getExtension(entry.getName()).equals("java")) {
				File dest = new File(parent, getName(entry.getName()));
				if(dest.exists()) {
					System.out.println("File already exists, skipping: " + dest.getAbsolutePath());
				}
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
