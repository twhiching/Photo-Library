package sample.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class loginController {
	
	Stage mainStage;
	
	@FXML         
	private Button login;
	
	@FXML         
	private TextField userName;

	public void start(Stage stage) {   
		mainStage = stage;
		
	}
	
	@FXML
	public void changeScene(ActionEvent evt) throws IOException {
		
		Parent adminView;
		if(userName.getText() == null || userName.getText().equals("") ) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("User name empty");
			alert.setContentText("User name cannot be empty!");
			alert.showAndWait();
		}else {
			//If the user is an admin, load up the admin page, else load up the user page
			if(userName.getText().equals("admin")) {
				adminView = FXMLLoader.load(getClass().getResource("/sample//view/adminPage.fxml"));
			}else {
				adminView = FXMLLoader.load(getClass().getResource("/sample//view/userPage.fxml"));
			}
			Scene scene = new Scene(adminView);
			Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();
			window.setScene(scene);
			window.show();
		}
	}
	
	
	
}
