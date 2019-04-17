package sample.view;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import sample.users.Default;

public class adminController implements Serializable {

	Stage mainStage;
	
	@FXML         
	private Button logout;

	@FXML
	private Button addUser;

	@FXML
	private Button deleteUser;

	@FXML
	private ListView<String> listView;

	private ObservableList<String> obsListAdmin;

	//Start method loads up all users to the list view
	public void start(Stage stage) {
		mainStage = stage;
		//Get current path of the user directory
				String dir = System.getProperty("user.dir");
		        String path = dir+"/src/sample/users/";
		        File currentDir = new File(path);
				//create an ObservableList from an ArrayList
				ArrayList<String> listOfUsers = getUserNames(currentDir);
				//Fill the observable list with the user names gathered from the file
				obsListAdmin =  FXCollections.observableArrayList(listOfUsers);
				listView.setItems(obsListAdmin);
	}
	
	@FXML
	public void logout(ActionEvent evt) throws IOException {

		Alert alert = new Alert(AlertType.CONFIRMATION, "Confirm logout ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/sample//view/loginPage.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			loginController Controller = loader.getController();
			Controller.start(mainStage);
			mainStage.setScene(new Scene(root, 250, 125));
			mainStage.setResizable(false);
			mainStage.show();
		}
	}

	@FXML
	public void addUser(ActionEvent evt) throws IOException {

		//Pop up dialog to get user name for the user object
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add new user");
		dialog.setHeaderText("Enter user name:");
		dialog.setContentText("Name:");
		Optional<String> result = dialog.showAndWait();
		//Create the path for which the users folder will reside
        String dir = System.getProperty("user.dir");
        String PATH = dir+"/src/sample/users/";
		String directoryName = PATH.concat(result.get().toLowerCase());
		File directory = new File(directoryName);
		// check if user have write permissions
        if(!directory.canWrite()) {
        	// check if the directory already exist
            if (! directory.exists()){
                boolean successful = directory.mkdirs();
                if (successful){
                    System.out.println("directory was created successfully");
                    try{
            			//Set up the path for the .ser file
            			directoryName = directoryName.concat("/"+result.get().toLowerCase()+".ser");
            			String filename = directoryName;
            			Default user = new Default(result.get());
            			//Saving of object in the .ser file
            			FileOutputStream file = new FileOutputStream(filename);
            			ObjectOutputStream out = new ObjectOutputStream(file);
            			// Method for serialization of object
            			out.writeObject(user);
            			out.close();
            			file.close();
            			System.out.println("Object has been serialized");
            			//Update the listView
            			adminUpdatelistView();
            		}
            		catch (IOException e){
            			System.out.println("IOException is caught");
            			e.printStackTrace();
            			System.exit(-1);
            		}
                }else{
                    System.out.println("directory was not created");
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialog");
					alert.setHeaderText("Directory was not created");
					alert.setContentText("An error occurred while creating the user directory, please try again");
					alert.showAndWait();
                }
            }
        }else{
            System.out.println("PERMISSION DENIED");
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("User already exists");
			alert.setContentText("User already exists in the system, please enter a unique user name");
			alert.showAndWait();
        }
	}

	@FXML
	public void deleteUser(ActionEvent evt) throws IOException {
		//Get the item and index value for deletion of the particular song
		String item = listView.getSelectionModel().getSelectedItem();
	    int index = listView.getSelectionModel().getSelectedIndex();
	    //Make sure an item is selected before being able to delete an entry
	    if(item == null || index == -1) {    	
	    	Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Must pick a user first");
			alert.setContentText("A user must be picked in order to delete them from the database!");
			alert.showAndWait();
	    }else {	
	    		
			Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + item + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				//Set up the path for which to search through
				String dir = System.getProperty("user.dir");
		        String path = dir+"/src/sample/users/";
		        File folder = new File(path);
				File[] files = folder.listFiles();
				//Check if user directory exist, if not return back empty userNames
				if(!folder.exists()) {
					return;
				}
				//Search through the directory and all its subdirectory's recursively to find all users folders, and thier .ser files
				for (File file : files) {
					if (file.isDirectory()) {
						//If directory, check to see if name matches, if so delete the directory
						System.out.println(file.getName());
						if(file.getName().equals(item)) {
							deleteFolder(file);
						}
					} else {
						//Else move on
						continue;
					}
				}
				adminUpdatelistView();
			}		  
	    }
	}
	
	private ArrayList<String> getUserNames(File currentDir){
		ArrayList<String> userNames = new ArrayList<>();
		String dir = System.getProperty("user.dir");
        String path = dir+"/src/sample/users/";
        File folder = new File(path);
		//File[] listOfFiles = folder.listFiles();
		File[] files = currentDir.listFiles();
		
        //Check if user directory exist, if not return back empty userNames
		if(!folder.exists()) {
			return userNames;
		}
		//Search through the directory and all its subdirectory's recursively to find all users folders, and their .ser files
		for (File file : files) {
			if (file.isDirectory()) {
				//Add up all your current results found in this level
				userNames.addAll(getUserNames(file));
			} else {
				//Check to see if file ends in .ser
				if (file.length() > 4){
				    String test = file.getName().substring(file.getName().length() - 4);
				    //If so split string and take the left hand side and add it to the list
				    if(test.equals(".ser")) {									  
				    	String[] arrOfStr = file.getName().split("\\.", 2); 
				    	 userNames.add(arrOfStr[0]);
				    }
				}
			}
		}		
		return userNames;
	}
	
	//I am not sure if this is deleting them as intended
	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { //some JVMs return null for empty dirs
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	            	//System.out.println("File: " + f.getName());
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	private void adminUpdatelistView() {
		String dir = System.getProperty("user.dir");
        String path = dir+"/src/sample/users/";
        File currentDir = new File(path);
		//create an ObservableList from an ArrayList
		ArrayList<String> listOfUsers = getUserNames(currentDir);
		obsListAdmin =  FXCollections.observableArrayList(listOfUsers);		
		listView.setItems(obsListAdmin);
	}
}