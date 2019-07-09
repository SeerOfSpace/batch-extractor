package com.seerofspace.batchextractor.core;

public interface UnzipperInterface {
	
	public void updateProgress(int count, int length);
	public void updateText(String file);
	public boolean errorConfirmation(String title, String text);
	public void errorReport(String title, String text);
	
}
