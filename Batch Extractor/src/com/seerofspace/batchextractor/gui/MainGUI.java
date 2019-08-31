package com.seerofspace.batchextractor.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainGUI extends Application {
	
	public static final String VERSION = "4.1";
	
	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainGUI.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Batch Extractor");
			primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("icons8-archive-80.png")));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}