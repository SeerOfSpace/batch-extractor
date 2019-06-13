package core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Unzipper {
	
	private UnzipperInterface unzipperInterface;
	private String[] fileExtensions;
	private boolean preserveFolders;
	private String[] keywords;
	
	public Unzipper() {
		this(new String[0], new String[0], true, new UnzipperInterface() {
			@Override
			public void updateProgress(int count, int length) {}
			@Override
			public void updateText(String file) {}
			@Override
			public boolean errorConfirmation(String title, String text) {return true;}
			@Override
			public void errorReport(String title, String text) {}
		});
	}
	
	public Unzipper(String[] fileExtensions, String[] keywords, boolean preserveFolders, UnzipperInterface unzipInterface) {
		this.unzipperInterface = unzipInterface;
		this.fileExtensions = fileExtensions;
		this.keywords = keywords;
		this.preserveFolders = preserveFolders;
	}
	
	public boolean unzip(File source) {
		File destFolder = new File(CustomFileUtils.removeExtension(source.getAbsolutePath()));
		destFolder = CustomFileUtils.getNextFreePath(destFolder);
		return unzip(source, destFolder);
	}
	
	public boolean unzip(File source, File destFolder) {
		destFolder.mkdirs();
		if(destFolder.isFile() || !destFolder.exists()) {
			unzipperInterface.errorReport("Error: Invalid destination path", destFolder.getAbsolutePath());
			return true;
		}
		if(!source.isFile() && CustomFileUtils.getExtension(source.getName()) != "zip") {
			unzipperInterface.errorReport("Error: Invalid zip file", source.getAbsolutePath());
			return true;
		}
		try {
			ZipFile zipFile = new ZipFile(source);
			Enumeration<? extends ZipEntry> zipEnum = zipFile.entries();
			int length = getEnumSize(zipFile.entries());
			int count = 0;
			while(zipEnum.hasMoreElements()) {
				ZipEntry entry = zipEnum.nextElement();
				unzipperInterface.updateText(CustomFileUtils.getName(entry.getName()));
				if(!entry.isDirectory() && isValid(entry)) {
					File dest;
					if(preserveFolders) {
						dest = new File(destFolder, entry.getName());
						dest.getParentFile().mkdirs();
					} else {
						dest = new File(destFolder, CustomFileUtils.getName(entry.getName()));
					}
					if(dest.exists()) {
						if(unzipperInterface.errorConfirmation("Error: file already exists" + "\nSkip and continue?", dest.getAbsolutePath())) {
							continue;
						} else {
							zipFile.close();
							CustomFileUtils.deleteFolder(destFolder);
							return false;
						}
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
				unzipperInterface.updateProgress(++count, length);
			}
			zipFile.close();
		} catch(IOException e1) {
			unzipperInterface.errorReport("Error: reading zip failed", source.getAbsolutePath());
		}
		return true;
	}
	
	private boolean isValid(ZipEntry entry) {
		return containsKeyword(entry) && hasValidExtension(entry);
	}
	
	private boolean containsKeyword(ZipEntry entry) {
		if(keywords.length == 0) {
			return true;
		}
		for(String keyword : keywords) {
			if(CustomFileUtils.getName(entry.getName()).toLowerCase().contains(keyword.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasValidExtension(ZipEntry entry) {
		if(fileExtensions.length == 0) {
			return true;
		}
		for(String extension : fileExtensions) {
			if(CustomFileUtils.getExtension(entry.getName()).equals(extension)) {
				return true;
			}
		}
		return false;
	}
	
	private int getEnumSize(Enumeration<?> enumeration) {
		int i = 0;
		while(enumeration.hasMoreElements()) {
			enumeration.nextElement();
			i++;
		}
		return i;
	}

	public UnzipperInterface getUnzipperInterface() {
		return unzipperInterface;
	}

	public void setUnzipperInterface(UnzipperInterface unzipInterface) {
		this.unzipperInterface = unzipInterface;
	}

	public String[] getFileExtensions() {
		return fileExtensions;
	}

	public void setFileExtensions(String[] fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public boolean isPreserveFolders() {
		return preserveFolders;
	}

	public void setPreserveFolders(boolean preserveFolders) {
		this.preserveFolders = preserveFolders;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
	
}
