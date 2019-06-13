package core;

public interface UnzipperFNFInterface {
	
	public void updateProgressFNF(int count, int length);
	public void updateTextFNF(String file);
	public boolean errorConfirmationFNF(String title, String text);
	public void errorReportFNF(String title, String text);
	
}
