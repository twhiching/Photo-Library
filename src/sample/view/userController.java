package sample.view;

import java.io.*;
import java.util.LinkedList;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Album;
import sample.Photo;
import sample.users.Default;

public class userController implements Serializable{



	private String user;
	
	@FXML         
	private Button logout;
	
	@FXML
	private Button addAlbum;
	
	@FXML
	private Button addPhoto;
	
	@FXML
	private Button editPhoto;

	@FXML
	private ListView listView;

	@FXML
	private GridPane gridPane;
	
	
	public void initialize(String userName) {

		//Load up all albums users may have and display them in the list view
		//Load up all photo's user may have and display them in the grid view
		//Select first photo in the list to be displayed in the photo's tab

		//Deserialize the user object first
		user = userName;
		System.out.println("User name is:"+user);

		//System.out.println("User is:" + user);
		//Image image = new Image("File:image/myfile.jpg")
		//gridpane.getChildren().add(new ImageView(image));


	}

	public void setUser(String userName){


	}
	
	@FXML
	public void logout(ActionEvent evt) throws IOException {
		
		//TODO add a pop up to confirm user choice of logging out
		Parent loginView = FXMLLoader.load(getClass().getResource("/sample//view/loginPage.fxml"));
		Scene scene = new Scene(loginView);
		Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	@FXML
	public void addAlbum(ActionEvent evt) throws IOException{
		
		//TODO add check to make sure that user is unique
		//Pop up dialog to get user name for the user object
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add New Album");
		dialog.setHeaderText("Enter Album Name:");
		dialog.setContentText("Album:");
		Optional<String> result = dialog.showAndWait();

		Default userAlbum = null;
		//No input validation to check for duplicate albums
		//Deserialize User to get access to user functions
		try {
			String dir = System.getProperty("user.dir");
	        String path = dir+"/src/sample/users/" + user + "/" + user + ".ser";
			FileInputStream file = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(file);
			System.out.println("Deserializing user");
			userAlbum = (Default)in.readObject();
			System.out.println("Adding new album");
			Album newAlbum = new Album(result.get());
			//userAlbum.addAlbum(newAlbum);
			System.out.println("Added album: " + result.get());
			in.close();
			file.close();
			
			//Reserialize again to save the new update
			System.out.println("Reserializing object");
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(userAlbum);
			out.close();
			fileOut.close();
			System.out.println("Succesfully serialized object");
			
			
			
		}catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void addPhoto(ActionEvent evt) throws IOException {
		//TODO add check to make sure that user is unique
		//Pop up dialog to get user name for the user object
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("File Explorer");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(
			    new FileChooser.ExtensionFilter("All Images", "*.*"),
			    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
			    new FileChooser.ExtensionFilter("GIF", "*.gif"),
			    new FileChooser.ExtensionFilter("BMP", "*.bmp"),
			    new FileChooser.ExtensionFilter("PNG", "*.png")
			);
		Window stage = null;
		//After selecting a file it saves the true path in this variable
		String photoPath = fileChooser.showOpenDialog(stage).toString();
		System.out.println(photoPath);
		
		Default userAlbum = null;
		//Below will be deserialization and serialization of object
	}
}
