package sample.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.Album;
import sample.Photo;

public class slideShowController {

	Stage mainStage;
	String userName;
	int index;
	Album album;
	EventHandler<WindowEvent> eventHandler;
	
	@FXML         
	private Button back;
	
	@FXML
	private Button foward;
	
	@FXML
	private Button cancel;
	
	@FXML
	private ImageView imageView;
	
	@FXML
	private TilePane tilePane;
	
	public void start(Stage stage, String user, int index,Album album) {   
		//Load up the first image to view for the user
		mainStage = stage;
		userName = user;
		this.index = index;
		this.album = album;
		eventHandler = new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {            	                          	                
				exit(event);
            }
        }; 
        mainStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,eventHandler);
		loadImage(index);
		
	}
	
	public void exit(WindowEvent event) {
		 Alert alert = new Alert(AlertType.CONFIRMATION, "Confirm closing down the program?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
	        alert.showAndWait();
	        if (alert.getResult() == ButtonType.YES) {	       
	            cancel(null);
	        }else if(alert.getResult() == ButtonType.NO) {
	        	event.consume();
	        }else if(alert.getResult() == ButtonType.CANCEL) {
	        	event.consume();
	        }
	}
	
	@FXML
	public void back(ActionEvent evt) {
		index = index - 1;
		if(index < 0) {
			//index = 0;
			index = album.getAlbumPhotos().size() - 1;
		}
		loadImage(index);
	}
	
	@FXML
	public void foward(ActionEvent evt) {
		index = index + 1;
		if(index > album.getAlbumPhotos().size() - 1) {
			//index = album.getAlbumPhotos().size() - 1;
			index = 0;
		}
		loadImage(index);
	}
	
	@FXML
	public void cancel(ActionEvent evt) {
		mainStage.removeEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,eventHandler);
		FXMLLoader loader = new FXMLLoader();
 		loader.setLocation(getClass().getResource("/sample//view/userPage.fxml"));	
		AnchorPane root;
		try {
			root = (AnchorPane)loader.load();
			userController Controller = loader.getController();
			Controller.resumeState(mainStage,userName,album,index);
	 		mainStage.setScene(new Scene(root, 1000, 500));
	 		mainStage.setResizable(false);
	 		mainStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
 		
	}
	
	public void loadImage(int index) {
		FileInputStream input;
		album.getAlbumPhotos();
		Photo currentPhoto = album.getPhoto(index);
		try {
			input = new FileInputStream(currentPhoto.getPhotoPath());
			Image image = new Image(input);
	        imageView.setImage(image);         
	        imageView.setFitWidth(900);
	        imageView.setFitHeight(665); 	  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	}
}
