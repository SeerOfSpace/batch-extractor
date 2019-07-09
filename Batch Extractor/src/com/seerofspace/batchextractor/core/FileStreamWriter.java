package com.seerofspace.batchextractor.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.seerofspace.utils.FileUtils;

public class FileStreamWriter implements StreamWriter {
	
	private BufferedOutputStream outputStream;
	private File dest;
	
	public FileStreamWriter() {
		outputStream = null;
		dest = null;
	}
	
	@Override
	public void open(File file) {
		if(file.isFile()) {
			throw new IllegalArgumentException("Must be a directory");
		}
		dest = file;
		dest.mkdirs();
	}

	@Override
	public void write(InputStream inputStream, String path) throws IOException {
		if(dest == null) {
			throw new RuntimeException("FileStreamWriter not opened");
		}
		File file = new File(dest, path);
		if(file.isFile()) {
			throw new IOException("duplicate");
		}
		if(FileUtils.isPathDir(path)) {
			file.mkdirs();
			return;
		}
		file.getParentFile().mkdirs();
		outputStream = new BufferedOutputStream(new FileOutputStream(file));
		byte[] buffer = new byte[1024];
		int len;
		while((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
	}

	@Override
	public void close() throws IOException {
		dest = null;
	}

}
