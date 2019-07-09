package com.seerofspace.batchextractor.core;

import java.util.Stack;
import java.util.zip.ZipEntry;

public interface ValidatorInterface {
	
	public boolean isValid(String inputPath, Stack<ZipEntry> entryStack);
	
}
