package gui;

import java.io.File;

import com.seerofspace.components.DirField;

import core.CustomFileUtils;
import core.Unzipper;
import core.UnzipperFNF;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainGUIController {
	
	@FXML private BorderPane root;
	@FXML private DirField dirField;
	@FXML private Button startButton;
	@FXML private CheckBox mossCheckBox;
	@FXML private CheckBox renameCheckBox;
	@FXML private CheckBox blackboardCheckBox;
	@FXML private RadioButton radioJava;
	@FXML private RadioButton radioCpp;
	
	@FXML
	private void initialize() {
		dirField.setExtensions(new ExtensionFilter("Zip Files", "*.zip"));
		ToggleGroup toggleGroup = new ToggleGroup();
		toggleGroup.getToggles().addAll(radioJava, radioCpp);
		
		Platform.runLater(() -> {
			root.requestFocus();
		});
		root.setOnMouseClicked(e -> {
			root.requestFocus();
			e.consume();
		});
		startButton.setOnAction(e -> {
			start();
			e.consume();
		});
		
		
	}
	
	private void start() {
		if(dirField.isValid()) {
			File source = new File(dirField.getText());
			ProgressGUI progressGUI = new ProgressGUI();
			progressGUI.start();
			ProgressGUIController progressGUIController = progressGUI.getController();
			Unzipper unzipper = new Unzipper();
			unzipper.setPreserveFolders(false);
			unzipper.setUnzipperInterface(progressGUIController);
			UnzipperFNF unzipperFNF = new UnzipperFNF();
			unzipperFNF.setUnzipper(unzipper);
			unzipperFNF.setUnzipperFNFInterface(progressGUIController);
			
			if(radioJava.isSelected()) {
				unzipper.setFileExtensions(new String[] {"java"});
			} else if(radioCpp.isSelected()) {
				unzipper.setFileExtensions(new String[] {"cpp", "h"});
			}
			if(mossCheckBox.isSelected()) {
				unzipperFNF.setKeywords(new String[] {"moss.zip"});
			}
			
			new Thread(() -> {
				File dest;
				boolean fnf;
				boolean result;
				if(blackboardCheckBox.isSelected() && source.isFile()) {
					fnf = true;
					progressGUIController.setFnf(fnf);
					File temp = new File(source.getParentFile(), "...temp");
					new Unzipper().unzip(source, temp);
					dest = getFileDest(source);
					result = unzipperFNF.unzipFNF(temp, dest);
					CustomFileUtils.deleteFolder(temp);
				} else if(source.isFile()) {
					fnf = false;
					progressGUIController.setFnf(fnf);
					dest = getFileDest(source);
					result = unzipper.unzip(source, dest);
				} else {
					fnf = true;
					progressGUIController.setFnf(fnf);
					dest = getFolderDest(source);
					result = unzipperFNF.unzipFNF(source, dest);
				}
				if(renameCheckBox.isSelected() && result) {
					rename(dest, fnf);
				}
				Platform.runLater(() -> {
					progressGUI.close();
				});
			}).start();
		}
	}
	
	private File getFileDest(File source) {
		File dest = new File(source.getParentFile(), CustomFileUtils.removeExtension(source.getName()));
		dest = CustomFileUtils.getNextFreePath(dest);
		return dest;
	}
	
	private File getFolderDest(File source) {
		File dest = new File(source.getParentFile(), source.getName() + " Unzipped");
		dest = CustomFileUtils.getNextFreePath(dest);
		return dest;
	}
	
	private void rename(File file, boolean fnf) {
		File[] files;
		if(fnf) {
			files = file.listFiles();
		} else {
			files = new File[] {file};
		}
		for(File entry : files) {
			String name = entry.getName();
			int index = name.indexOf('_', 1 + name.indexOf('_', 1 + name.indexOf('_', 1 + name.indexOf('_'))));
			String newName = name.substring(index + 1, name.length());
			File newFile = new File(entry.getParent(), newName);
			newFile = CustomFileUtils.getNextFreePath(newFile);
			entry.renameTo(newFile);
		}
	}
	
}
