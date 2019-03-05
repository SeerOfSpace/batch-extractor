package gui;

import java.io.File;
import java.io.IOException;

import com.seerofspace.components.DirField;

import core.UnzipLogic;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainGUIController {
	
	@FXML BorderPane root;
	@FXML DirField dirField;
	@FXML Button startButton;
	@FXML CheckBox checkBox;
	
	@FXML
	private void initialize() {
		Platform.runLater(() -> {
			root.requestFocus();
		});
		root.setOnMouseClicked(e -> {
			root.requestFocus();
			e.consume();
		});
		startButton.setOnAction(e -> {
			if(dirField.isValid()) {
				File file = new File(dirField.getText());
				try {
					if(file.isDirectory()) {
						UnzipLogic.unzipFolderJava(file, checkBox.isSelected());
					} else {
						UnzipLogic.unzipJava(file);
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
