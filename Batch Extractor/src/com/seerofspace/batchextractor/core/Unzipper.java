package com.seerofspace.batchextractor.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.seerofspace.utils.FileUtils;

public class Unzipper {
	
	private UnzipperInterface unzipperInterface;
	private StreamWriter streamWriter;
	private ValidatorInterface validatorInterface;
	private PathModifierInterface pathModifierInterface;
	private boolean zipRecursion;
	private boolean stop;
	private Stack<ZipEntry> entryStack;
	private int length;
	private int count;
	
	public Unzipper() {
		this(new FileStreamWriter(), false, (inputPath, entryStack) -> {return true;}, (outputPath, entryStack) -> {return outputPath;}, new UnzipperInterface() {
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
	
	public Unzipper(StreamWriter streamWriter, boolean zipRecursion, ValidatorInterface validatorInterface, PathModifierInterface pathModifierInterface, UnzipperInterface unzipInterface) {
		this.streamWriter = streamWriter;
		this.unzipperInterface = unzipInterface;
		this.zipRecursion = zipRecursion;
		this.validatorInterface = validatorInterface;
		this.pathModifierInterface = pathModifierInterface;
		stop = false;
	}
	
	public boolean unzip(File source, File dest) {
		stop = false;
		if(!FileUtils.getExtension(source).equals("zip")) {
			unzipperInterface.errorReport("Error: not a zip file", source.getAbsolutePath());
			return false;
		}
		streamWriter.open(dest);
		entryStack = new Stack<ZipEntry>();
		count = 0;
		try {
			length = getLength(new FileInputStream(source));
			unzipperInterface.updateProgress(count++, length);
			unzipRecursion(new FileInputStream(source));
			streamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(stop) {
			return false;
		}
		return true;
	}
	
	private void unzipRecursion(InputStream inputStream) {
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		ZipEntry entry;
		int num = 0;
		try {
			while((entry = zipInputStream.getNextEntry()) != null) {
				if(stop) {
					return;
				}
				num++;
				unzipperInterface.updateText("../" + getInputPath(entry));
				unzipperInterface.updateProgress(count++, length);
				if(!validatorInterface.isValid(getInputPath(entry), entryStack)) {
					continue;
				}
				if(zipRecursion && !entry.isDirectory() && FileUtils.getExtension(entry.getName()).equals("zip")) {
					entryStack.push(entry);
					unzipRecursion(zipInputStream);
					entryStack.pop();
				} else {
					String path = pathModifierInterface.modify(getOutputPath(entry), entryStack);
					try {
						streamWriter.write(zipInputStream, path);
					} catch(IOException e) {
						if(e.getMessage().equals("duplicate")) {
							errorConfirmation("Error: duplicate file" + "\n" + "Skip and continue?", "../" + path);
						} else {
							errorConfirmation("Error: could not write contents" + "\n" + "Skip and continue?", "../" + path);
						}
					}
				}
			}
		} catch (IOException e) {
			errorConfirmation("Error: could not unzip file" + "\n" + "Skip and continue?", "../" + getInputPath());
		}
		if(num == 0) {
			errorConfirmation("Error: zip file is empty" + "\n" + "Skip and continue?", "../" + getInputPath());
		}
	}
	
	private int getLength(InputStream inputStream) {
		int count = 0;
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		ZipEntry entry;
		try {
			while((entry = zipInputStream.getNextEntry()) != null) {
				count++;
				if(zipRecursion && !entry.isDirectory() && FileUtils.getExtension(entry.getName()).equals("zip")) {
					count += getLength(zipInputStream);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	private String getInputPath(ZipEntry entry) {
		return getInputPath() + "/" + entry.getName();
	}
	
	private String getInputPath() {
		String path = "";
		for(ZipEntry entry : entryStack) {
			path += entry.getName() + "/";
		}
		if(!path.equals("")) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}
	
	private String getOutputPath(ZipEntry entry) {
		String path = "";
		for(ZipEntry temp : entryStack) {
			path += FileUtils.removeExtension(temp.getName()) + "/";
		}
		return path + entry.getName();
	}
	
	private void errorConfirmation(String title, String text) {
		boolean b = unzipperInterface.errorConfirmation(title, text);
		if(!b) {
			stop();
		}
	}
	
	public void stop() {
		stop = true;
	}
	
	public UnzipperInterface getUnzipperInterface() {
		return unzipperInterface;
	}

	public void setUnzipperInterface(UnzipperInterface unzipInterface) {
		this.unzipperInterface = unzipInterface;
	}

	public StreamWriter getStreamWriter() {
		return streamWriter;
	}

	public void setStreamWriter(StreamWriter streamWriter) {
		this.streamWriter = streamWriter;
	}

	public ValidatorInterface getValidatorInterface() {
		return validatorInterface;
	}

	public void setValidatorInterface(ValidatorInterface validatorInterface) {
		this.validatorInterface = validatorInterface;
	}

	public PathModifierInterface getPathModifierInterface() {
		return pathModifierInterface;
	}

	public void setPathModifierInterface(PathModifierInterface pathModifierInterface) {
		this.pathModifierInterface = pathModifierInterface;
	}

	public boolean isZipRecursion() {
		return zipRecursion;
	}

	public void setZipRecursion(boolean zipRecursion) {
		this.zipRecursion = zipRecursion;
	}
	
}
