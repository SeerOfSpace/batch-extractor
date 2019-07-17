package com.seerofspace.batchextractor.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.seerofspace.batchextractor.core.FileStreamWriter;
import com.seerofspace.batchextractor.core.Unzipper;
import com.seerofspace.batchextractor.core.ZipStreamWriter;
import com.seerofspace.components.javafx.DirField;
import com.seerofspace.utils.FileUtils;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainGUIController {
	
	@FXML private BorderPane root;
	@FXML private DirField dirField;
	@FXML private Button startButton;
	@FXML private CheckBox zipExport;
	@FXML private RadioButton javaRadio;
	@FXML private RadioButton cppRadio;
	@FXML private RadioButton customRadio;
	@FXML private TextField customText;
	@FXML private ToggleGroup extensionGroup;
	@FXML private MenuBar menuBar;
	@FXML private Menu fileMenu;
	@FXML private Menu helpMenu;
	@FXML private MenuItem exitMenuItem;
	@FXML private MenuItem aboutMenuItem;
	
	@FXML
	private void initialize() {
		dirField.setExtensions(new ExtensionFilter("Zip Files", "*.zip"));
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
		exitMenuItem.setOnAction(e -> {
			Platform.exit();
			e.consume();
		});
		aboutMenuItem.setOnAction(e -> {
			Alert aboutDialog = new Alert(AlertType.INFORMATION);
			aboutDialog.setHeaderText("Version " + MainGUI.VERSION);
			aboutDialog.showAndWait();
			e.consume();
		});
	}
	
	private void start() {
		if(dirField.isValid()) {
			ProgressGUI progressGUI = new ProgressGUI();
			progressGUI.start();
			ProgressGUIController progressGUIController = progressGUI.getController();
			Unzipper unzipper = new Unzipper();
			unzipper.setUnzipperInterface(progressGUIController);
			unzipper.setZipRecursion(true);
			if(zipExport.isSelected()) {
				unzipper.setStreamWriter(new ZipStreamWriter());
			} else {
				unzipper.setStreamWriter(new FileStreamWriter());
			}
			progressGUIController.setOnStopButtonAction(e -> {
				unzipper.stop();
				e.consume();
			});
			progressGUI.setOnCloseRequest(e -> {
				unzipper.stop();
				e.consume();
			});
			
			List<String> extensionList = new ArrayList<>();
			if(extensionGroup.getSelectedToggle() == javaRadio) {
				extensionList.add("java");
			} else if(extensionGroup.getSelectedToggle() == cppRadio) {
				extensionList.add("cpp");
				extensionList.add("h");
			} else if(extensionGroup.getSelectedToggle() == customRadio) {
				addCustomExtensions(extensionList);
			}
			
			unzipper.setValidatorInterface((inputPath, entryStack) -> {
				if(!FileUtils.isPathDir(inputPath) && FileUtils.getExtension(inputPath).equals("zip")) {
					return true;
				}
				String extension = FileUtils.getExtension(inputPath);
				if(!FileUtils.isPathDir(inputPath) && !entryStack.empty()) {
					for(String temp : extensionList) {
						if(extension.equals(temp)) {
							return true;
						}
					}
				}
				return false;
			});
			
			unzipper.setPathModifierInterface((outputPath, entryStack) -> {
				String s = FileUtils.removeExtension(FileUtils.getName(entryStack.get(0).getName()));
				s += "/" + FileUtils.getName(outputPath);
				int index = s.indexOf('_', 1 + s.indexOf('_', 1 + s.indexOf('_', 1 + s.indexOf('_'))));
				s = s.substring(index + 1, s.length());
				return s;
			});
			
			new Thread(() -> {
				File source = new File(dirField.getText());
				File dest;
				String newName = FileUtils.removeExtension(source.getName()) + " Extracted";
				if(zipExport.isSelected()) {
					dest = new File(FileUtils.getNewFileName(source, newName));
				} else {
					dest = new File(source.getParentFile(), newName);
				}
				dest = FileUtils.getNextFreePath(dest);
				unzipper.unzip(source, dest);
				Platform.runLater(() -> {
					progressGUI.close();
				});
			}).start();
		}
	}
	
	private void addCustomExtensions(List<String> extensionList) {
		Scanner scanner = new Scanner(customText.getText());
		while(scanner.hasNext()) {
			extensionList.add(scanner.next());
		}
		scanner.close();
	}
	
}
