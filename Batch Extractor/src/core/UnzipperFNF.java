package core;

import java.io.File;

public class UnzipperFNF {
	
	private Unzipper unzipper;
	private String[] keywords;
	private UnzipperFNFInterface unzipperFNFInterface;
	
	public UnzipperFNF() {
		this(new Unzipper());
	}
	
	public UnzipperFNF(Unzipper unzipper) {
		this(unzipper, new String[0], new UnzipperFNFInterface() {
			@Override
			public void updateProgressFNF(int count, int length) {}
			@Override
			public void updateTextFNF(String file) {}
			@Override
			public boolean errorConfirmationFNF(String title, String text) {return true;}
			@Override
			public void errorReportFNF(String title, String text) {}
		});
	}
	
	public UnzipperFNF(Unzipper unzipper, String[] keywords, UnzipperFNFInterface unzipperFNFInterface) {
		this.unzipper = unzipper;
		this.keywords = keywords;
		this.unzipperFNFInterface = unzipperFNFInterface;
	}
	
	public boolean unzipFNF(File sourceFNF) {
		File destFNF = new File(sourceFNF.getParentFile(), sourceFNF.getName() + " Unzipped");
		destFNF = CustomFileUtils.getNextFreePath(destFNF);
		return unzipFNF(sourceFNF, destFNF);
	}
	
	public boolean unzipFNF(File sourceFNF, File destFNF) {
		destFNF.mkdirs();
		//insert error check for parameters
		int length = CustomFileUtils.getFileCount(sourceFNF);
		boolean b = unzipFNFRecursion(sourceFNF, destFNF, 0, length);
		if(!b) {
			CustomFileUtils.deleteFolder(destFNF);
		}
		return b;
	}
	
	private boolean unzipFNFRecursion(File sourceFNF, File destFNF, int count, int length) {
		for(File file : sourceFNF.listFiles()) {
			if(file.isDirectory()) {
				if(!unzipFNFRecursion(file, destFNF, count, length)) {
					return false;
				}
			} else {
				unzipperFNFInterface.updateTextFNF(file.getName());
				if(isValid(file)) {
					File destFolder = new File(destFNF, CustomFileUtils.removeExtension(file.getName()));
					if(!unzipper.unzip(file, destFolder)) {
						return false;
					}
				}
				unzipperFNFInterface.updateProgressFNF(++count, length);
			}
		}
		return true;
	}
	
	private boolean isValid(File file) {
		if(!CustomFileUtils.getExtension(file.getName()).equals("zip")) {
			return false;
		}
		if(keywords.length == 0) {
			return true;
		}
		for(String keyword : keywords) {
			if(file.getName().toLowerCase().contains(keyword.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	
	public Unzipper getUnzipper() {
		return unzipper;
	}

	public void setUnzipper(Unzipper unzipper) {
		this.unzipper = unzipper;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}

	public UnzipperFNFInterface getUnzipperFNFInterface() {
		return unzipperFNFInterface;
	}

	public void setUnzipperFNFInterface(UnzipperFNFInterface unzipperFNFInterface) {
		this.unzipperFNFInterface = unzipperFNFInterface;
	}
	
}
