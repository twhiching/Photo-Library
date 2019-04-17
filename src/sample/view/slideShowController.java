package sample.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import sample.Album;
import sample.Photo;

public class slideShowController {

	Stage mainStage;
	String userName;
	int index;
	Album album;
	
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
		//System.out.println("User is: " + user);
		//System.out.println("Album is: " + album.getName());
		//System.out.println("Album size is: "+ album.getAlbumPhotos().size());
		//System.out.println("index of photo is: " + index);
		loadImage(index);
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
