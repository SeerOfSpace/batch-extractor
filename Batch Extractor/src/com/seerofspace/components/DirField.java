package com.seerofspace.components;

import java.io.File;
import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class DirField extends HBox {
	
	@FXML private TextField textfield;
	@FXML private Button fileButton;
	@FXML private Button folderButton;
	private String defaultText;
	private ExtensionFilter[] extensions;
	private PseudoClass empty;
	private PseudoClass dragAccepted;
	private PseudoClass dragRejected;
	private BooleanProperty enableFileButton;
	private BooleanProperty enableFolderButton;
	
	public DirField() {
		this("");
	}
	
	public DirField(String defaultText) {
		this(defaultText, true, true);
	}
	
	public DirField(String defaultText, boolean enableFileButton, boolean enableFolderButton) {
		this(defaultText, enableFileButton, enableFolderButton, new ExtensionFilter("All Files", "*.*"));
	}
	
	public DirField(String defaultText, boolean enableFileButton, boolean enableFolderButton, ExtensionFilter... extensions) {
		super();
		this.defaultText = defaultText;
		this.extensions = extensions;
		this.enableFileButton = new SimpleBooleanProperty(enableFileButton);
		this.enableFolderButton = new SimpleBooleanProperty(enableFolderButton);
		initialize();
	}
	
	private void initialize() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DirField.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
		dragAccepted = PseudoClass.getPseudoClass("dragAccepted");
		dragRejected = PseudoClass.getPseudoClass("dragRejected");
		empty = PseudoClass.getPseudoClass("empty");
		textfield.setText(defaultText);
		textfield.pseudoClassStateChanged(empty, true);
		textfield.focusedProperty().addListener((arg, ov, nv) -> {
			if(nv) {
				if(textfield.getText().equals(defaultText)) {
					textfield.setText("");
					textfield.pseudoClassStateChanged(empty, false);
				} else {
					Platform.runLater(()-> {
						textfield.selectAll();
					});
				}
			} else {
				if(textfield.getText().equals("")) {
					textfield.setText(defaultText);
					textfield.pseudoClassStateChanged(empty, true);
				}
			}
		});
		textfield.setOnDragOver(e -> {
			if(e.getDragboard().hasFiles() && isValid(e.getDragboard().getFiles().get(0))) {
				e.acceptTransferModes(TransferMode.LINK);
			}
			e.consume();
		});
		textfield.setOnDragDropped(e -> {
			boolean success = false;
			if(e.getDragboard().hasFiles()) {
				textfield.setText(e.getDragboard().getFiles().get(0).getAbsolutePath());
				textfield.pseudoClassStateChanged(empty, false);
				success = true;
			}
			e.setDropCompleted(success);
			e.consume();
		});
		textfield.setOnDragEntered(e -> {
			if(isValid(e.getDragboard().getFiles().get(0))) {
				textfield.pseudoClassStateChanged(dragAccepted, true);
			} else {
				textfield.pseudoClassStateChanged(dragRejected, true);
			}
			e.consume();
		});
		textfield.setOnDragExited(e -> {
			textfield.pseudoClassStateChanged(dragAccepted, false);
			textfield.pseudoClassStateChanged(dragRejected, false);
			e.consume();
		});
		fileButton.setOnAction(e -> {
			FileChooser filechooser = new FileChooser();
			filechooser.getExtensionFilters().addAll(extensions);
			File file = filechooser.showOpenDialog(this.getScene().getWindow());
			if(file != null) {
				textfield.setText(file.getAbsolutePath());
				textfield.pseudoClassStateChanged(empty, false);
			}
			e.consume();
		});
		folderButton.setOnAction(e -> {
			DirectoryChooser filechooser = new DirectoryChooser();
			File file = filechooser.showDialog(this.getScene().getWindow());
			if(file != null) {
				textfield.setText(file.getAbsolutePath());
				textfield.pseudoClassStateChanged(empty, false);
			}
			e.consume();
		});
		enableFileButton.addListener((ChangeListener) -> {
			if(enableFileButton.get()) {
				this.getChildren().add(1, fileButton);
			} else {
				this.getChildren().remove(fileButton);
			}
		});
		enableFolderButton.addListener((ChangeListener) -> {
			if(enableFolderButton.get()) {
				this.getChildren().add(2, folderButton);
			} else {
				this.getChildren().remove(folderButton);
			}
		});
	}
	
	private static String getExtension(String s) {
		return s.substring(s.lastIndexOf('.') + 1, s.length());
	}
	
	public boolean isValid() {
		File file = new File(textfield.getText());
		return isValid(file);
	}
	
	private boolean isValid(File file) {
		if(file.isDirectory()) {
			return true;
		} else if(file.isFile()) {
			String extension = "*." + getExtension(file.getAbsolutePath());
			for(ExtensionFilter e : extensions) {
				for(String s : e.getExtensions()) {
					if(s.equals(extension) || s.equals("*.*")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public String getText() {
		return textfield.getText();
	}
	
	public void setText(String text) {
		defaultText = text;
		if(textfield.getPseudoClassStates().contains(empty)) {
			textfield.setText(defaultText);
		}
	}
	
	public boolean getEnableFileButton() {
		return enableFileButton.get();
	}
	
	public void setEnableFileButton(boolean enableFileButton) {
		this.enableFileButton.set(enableFileButton);
	}
	
	public boolean getEnableFolderButton() {
		return enableFolderButton.get();
	}
	
	public void setEnableFolderButton(boolean enableFolderButton) {
		this.enableFolderButton.set(enableFolderButton);
	}
	
	public ExtensionFilter[] getExtensions() {
		return extensions;
	}
	
	public void setExtensions(ExtensionFilter... extensions) {
		this.extensions = extensions;
	}
	
}