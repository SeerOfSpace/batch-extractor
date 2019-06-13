package core;

import java.io.File;
import gui.MainGUI;

public class Main {
	
	public static void main(String[] args) {
		if(args.length == 0) {
			MainGUI.launchGUI();
		} else if(args.length > 2) {
			System.out.println("Error: Too many arguments");
		} else {
			File source = new File(args[0]);
			Unzipper unzipper = new Unzipper();
			UnzipperFNF unzipperFNF = new UnzipperFNF(unzipper);
			if(args.length == 2) {
				File dest = new File(args[2]);
				if(source.isDirectory()) {
					unzipperFNF.unzipFNF(source, dest);
				} else {
					unzipper.unzip(source, dest);
				}
			} else {
				if(source.isDirectory()) {
					unzipperFNF.unzipFNF(source);
				} else {
					unzipper.unzip(source);
				}
			}
		}
	}
	
}
