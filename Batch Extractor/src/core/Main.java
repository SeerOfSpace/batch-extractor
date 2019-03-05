package core;

import java.io.File;
import java.io.IOException;
import gui.MainGUI;

public class Main {
	
	public static void main(String[] args) {
		if(args.length == 0) {
			MainGUI.launchGUI();
		} else if(args.length > 1) {
			System.out.println("Error: Too many arguments");
		} else {
			File source = new File(args[0]);
			try {
				if(source.isDirectory()) {
					UnzipLogic.unzipFolderJava(source, true);
				} else if(source.isFile()) {
					UnzipLogic.unzipJava(source);
				} else {
					System.out.println("Error: Invalid path");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
