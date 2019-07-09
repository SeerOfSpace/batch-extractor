package com.seerofspace.batchextractor.core;

import java.util.Stack;
import java.util.zip.ZipEntry;

public interface PathModifierInterface {
	
	public String modify(String outputPath, Stack<ZipEntry> entryStack);
	
}
