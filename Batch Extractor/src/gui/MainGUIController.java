package gui;

import java.io.File;
import java.io.IOException;

import com.seerofspace.components.DirField;

import core.UnzipLogicCpp;
import core.UnzipLogicJava;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainGUIController {
	
	@FXML BorderPane root;
	@FXML DirField dirField;
	@FXML Button startButton;
	@FXML CheckBox checkBox;
	@FXML RadioButton radioJava;
	@FXML RadioButton radioCpp;
	
	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			root.requestFocus();
		});
		root.setOnMouseClicked(e -> {
			root.requestFocus();
			e.consume();
		});
		ToggleGroup toggleGroup = new ToggleGroup();
		toggleGroup.getToggles().addAll(radioJava, radioCpp);
		startButton.setOnAction(e -> {
			if(dirField.isValid()) {
				File file = new File(dirField.getText());
				try {
					if(radioJava.isSelected()) {
						if(file.isDirectory()) {
							UnzipLogicJava.unzipFolderJava(file, checkBox.isSelected());
						} else {
							UnzipLogicJava.unzipJava(file);
						}
					} else if(radioCpp.isSelected()) {
						if(file.isDirectory()) {
							UnzipLogicCpp.unzipFolderCpp(file, checkBox.isSelected());
						} else {
							UnzipLogicCpp.unzipCpp(file);
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			e.consume();
		});
		dirField.setExtensions(new ExtensionFilter("Zip Files", "*.zip"));
	}
	
}
