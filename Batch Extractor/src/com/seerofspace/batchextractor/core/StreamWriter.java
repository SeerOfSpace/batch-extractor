package com.seerofspace.batchextractor.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface StreamWriter {
	
	public void open(File file);
	public void write(InputStream inputStream, String path) throws IOException;
	public void close() throws IOException;
	
}
