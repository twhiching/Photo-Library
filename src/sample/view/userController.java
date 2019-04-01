package sample.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import javafx.stage.Stage;
import sample.Album;

public class userController {

	Stage mainStage;
	
	@FXML         
	private Button logout;
	
	@FXML
	private Button addAlbum;
	
	@FXML
	private Button addPhoto;
	
	@FXML
	private Button editPhoto;
	
	
	public void start(Stage stage) {   
		mainStage = stage;
		
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
		dialog.setHeaderText("Enter Albumn Name:");
		dialog.setContentText("Albumn:");
		Optional<String> result = dialog.showAndWait();
		
		//Create the path for which the users folder will reside
        String dir = System.getProperty("user.dir");
        String PATH = dir+"/src/sample/users/";
		String directoryName = PATH.concat(result.get());
		
		//TODO check to see if user name that admin inputed is unique!
		File directory = new File(directoryName);
	}
}
