package sample.view;

import java.io.IOException;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
				Scene scene = new Scene(adminView);
				Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();
				window.setScene(scene);
				window.show();
			}else {
				//Check if a specific user exists
				String dir = System.getProperty("user.dir");
		        String path = dir+"/src/sample/users/";
				File userDir = new File(path);
				String[] directories = userDir.list(new FilenameFilter() {
				  @Override
				  public boolean accept(File current, String name) {
				    return new File(current, name).isDirectory();
				  }
				});
				//Really messy but converts all directories in arraylist to lowercase so it's not case sensative
				ArrayList<String> userList = (ArrayList<String>) new ArrayList<String>(Arrays.asList(directories)).stream().map(String::toLowerCase)
                        																		.collect(Collectors.toList());;
				if(userList.contains(userName.getText())) {
					adminView = FXMLLoader.load(getClass().getResource("/sample//view/userPage.fxml"));
					System.out.println("Logging in as " + userName.getText());
					Scene scene = new Scene(adminView);
					Stage window = (Stage) ((Node)evt.getSource()).getScene().getWindow();
					window.setScene(scene);
					window.show();
				}else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialog");
					alert.setHeaderText("Invalid Username");
					alert.setContentText(userName.getText() + " does not exist");
					alert.showAndWait();
				}
			}
		}
	}
	
	
	
}
