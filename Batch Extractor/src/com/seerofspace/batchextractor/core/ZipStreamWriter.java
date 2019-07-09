package com.seerofspace.batchextractor.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.seerofspace.utils.FileUtils;

public class ZipStreamWriter implements StreamWriter {
	
	private ZipOutputStream outputStream;
	
	public ZipStreamWriter() {
		outputStream = null;
	}
	
	@Override
	public void open(File file) {
		if(!FileUtils.getExtension(file).equals("zip")) {
			throw new IllegalArgumentException("Must be a zip file");
		}
		try {
			outputStream = new ZipOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(InputStream inputStream, String path) throws IOException {
		if(outputStream == null) {
			throw new RuntimeException("ZipStreamWriter not opened");
		}
		ZipEntry entry = new ZipEntry(path);
		try {
			outputStream.putNextEntry(entry);
		} catch(IOException e) {
			if(e.getMessage().startsWith("duplicate")) {
				throw new IOException("duplicate");
			} else {
				throw e;
			}
		}
		byte[] buffer = new byte[1024];
		int len;
		while((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.flush();
	}
	
	@Override
	public void close() throws IOException {
		if(outputStream != null) {
			outputStream.close();
		}
	}

}
