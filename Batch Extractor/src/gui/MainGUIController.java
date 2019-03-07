package gui;

import java.io.File;
import java.io.IOException;

import com.seerofspace.components.DirField;

import core.OtherLogic;
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
					if(file.isFile() && blackboardCheckBox.isSelected()) {
						file = OtherLogic.unzipMainZip(file);
					}
					if(radioJava.isSelected()) {
						if(file.isDirectory()) {
							UnzipLogicJava.unzipFolderJava(file, mossCheckBox.isSelected());
						} else {
							UnzipLogicJava.unzipJava(file);
						}
					} else if(radioCpp.isSelected()) {
						if(file.isDirectory()) {
							UnzipLogicCpp.unzipFolderCpp(file, mossCheckBox.isSelected());
						} else {
							UnzipLogicCpp.unzipCpp(file);
						}
					}
					if(file.isDirectory() && renameCheckBox.isSelected()) {
						OtherLogic.rename(new File(file.getAbsolutePath() + " Unzipped"));
					}
					if(blackboardCheckBox.isSelected()) {
						OtherLogic.deleteFolder(file);
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
