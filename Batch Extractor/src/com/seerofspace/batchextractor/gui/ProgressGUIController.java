package com.seerofspace.batchextractor.gui;

import com.seerofspace.batchextractor.core.UnzipperInterface;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class ProgressGUIController implements UnzipperInterface {
	
	@FXML private Label label1;
	@FXML private Label label2;
	@FXML private ProgressBar progressBar;
	@FXML private Button stopButton;
	
	@FXML
	public void initialize() {
		label1.setText("Loading...");
		label2.setText("Loading...");
	}
	
	public void setOnStopButtonAction(EventHandler<ActionEvent> e) {
		stopButton.setOnAction(e);
	}
	
	@Override
	public void updateProgress(int count, int length) {
		Platform.runLater(()-> {
			label1.setText(count + " / " + length);
			if(count == 0) {
				progressBar.setProgress(0);
			} else {
				progressBar.setProgress((double) count / length);
			}
		});
	}

	@Override
	public void updateText(String file) {
		Platform.runLater(()-> {
			label2.setText(file);
		});
	}
	
	@Override
	public boolean errorConfirmation(String title, String text) {
		return new AlertThread(title, text, AlertType.CONFIRMATION).r();
	}

	@Override
	public void errorReport(String title, String text) {
		new AlertThread(title, text, AlertType.ERROR).r();
	}
	
	private class AlertThread extends Thread {
		private boolean b;
		private String text;
		private String title;
		private AlertType alertType;
		
		public AlertThread(String title, String text, AlertType alertType) {
			b = true;
			this.title = title;
			this.text = text;
			this.alertType = alertType;
		}
		
		public synchronized boolean r() {
			if(Platform.isFxApplicationThread()) {
				throw new RuntimeException("Called from application thread");
			}
			Platform.runLater(this);
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return b;
		}
		
		private synchronized void work() {
			Alert alert = new Alert(alertType, text);
			alert.setHeaderText(title);
			alert.showAndWait();
			if(alert.getResult() == ButtonType.OK) {
				b = true;
			} else {
				b = false;
			}
			notify();
		}
		
		@Override
		public void run() {
			work();
		}
	}
	
}
