package com.seerofspace.components.javafx;

import java.io.File;
import java.io.IOException;

import javafx.application.Platform;
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
	
	public enum Type {
		FILES, FOLDERS, BOTH;
	}
	
	@FXML private TextField textfield;
	@FXML private Button fileButton;
	@FXML private Button folderButton;
	private ExtensionFilter[] extensions;
	private String promptText;
	private PseudoClass dragAccepted;
	private PseudoClass dragRejected;
	private Type type;
	
	public DirField() {
		this("");
	}
	
	public DirField(String promptText) {
		this(promptText, Type.BOTH);
	}
	
	public DirField(String promptText, Type type) {
		this(promptText, type, new ExtensionFilter("All Files", "*.*"));
	}
	
	public DirField(String promptText, Type type, ExtensionFilter... extensions) {
		super();
		this.promptText = promptText;
		this.type = type;
		this.extensions = extensions;
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
		textfield.setPromptText(promptText);
		setType(type);
		textfield.focusedProperty().addListener((arg, ov, nv) -> {
			if(nv) {
				if(!textfield.getText().equals("")) {
					Platform.runLater(()-> {
						textfield.selectAll();
					});
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
			}
			e.consume();
		});
		folderButton.setOnAction(e -> {
			DirectoryChooser filechooser = new DirectoryChooser();
			File file = filechooser.showDialog(this.getScene().getWindow());
			if(file != null) {
				textfield.setText(file.getAbsolutePath());
			}
			e.consume();
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
		if(file.isDirectory() && (type == Type.FOLDERS || type == Type.BOTH)) {
			return true;
		} else if(file.isFile() && (type == Type.FILES || type == Type.BOTH)) {
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
		textfield.setText(text);
	}
	
	public String getPromptText() {
		return textfield.getPromptText();
	}
	
	public void setPromptText(String text) {
		textfield.setPromptText(text);
	}
	
	public ExtensionFilter[] getExtensions() {
		return extensions;
	}
	
	public void setExtensions(ExtensionFilter... extensions) {
		this.extensions = extensions;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
		this.getChildren().removeAll(fileButton, folderButton);
		if(type == Type.FILES) {
			this.getChildren().add(fileButton);
		} else if(type == Type.FOLDERS) {
			this.getChildren().add(folderButton);
		} else if(type == Type.BOTH) {
			this.getChildren().addAll(fileButton, folderButton);
		}
	}
	
}