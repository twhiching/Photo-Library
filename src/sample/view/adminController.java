package sample.view;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
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

	private ObservableList<String> obsList;

	//Start method loads up all users to the list view
	public void start(Stage stage) {   
		mainStage = stage;

		//create an ObservableList from an ArrayList
		ArrayList<String> listOfUsers = new ArrayList<>();
		/*try {
			//Get user names from serialized file
			listOfUsers = fileCreator.songNames();
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//Fill the observable list with the user names gathered from the file
		obsList =  FXCollections.observableArrayList(listOfUsers);
		listView.setItems(obsList);

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
	public void addUser(ActionEvent evt) throws IOException {

		//Pop up dialog to get user name for the user object
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add new user");
		dialog.setHeaderText("Enter user name:");
		dialog.setContentText("Name:");
		Optional<String> result = dialog.showAndWait();

		//Create the folder for this user in the user folder !!!! Also check to see if user is unquie, do that later!!!!
        String dir = System.getProperty("user.dir");
        String PATH = dir+"/src/sample/users/";
		String directoryName = PATH.concat(result.get());

		System.out.println(directoryName);

		//TODO check to see if user name that admin inputed is unquie!
		File directory = new File(directoryName);
        if(!directory.canWrite()) { // check if user have write permissions
            if (! directory.exists()){
                boolean successful = directory.mkdirs();
                if (successful){
                    System.out.println("directory was created successfully");
                }else{
                    System.out.println("directory was not created");
                }
                // If you require it to make the entire directory path including parents,
                // use directory.mkdirs(); here instead.
            }
        }else{
            System.out.println("PERMISSION DENIED");
        }

		//Folder is created, create user object and serialize it and save it in the directory of that user
		try{
			//Set up the path for the .ser file
			directoryName = directoryName.concat("/"+result.get()+".ser");
			String filename = directoryName;
			Default user = new Default(result.get());
			//Saving of object in a file
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);

			// Method for serialization of object
			out.writeObject(user);
			out.close();
			file.close();
			System.out.println("Object has been serialized");
		}
		catch (IOException e){
			System.out.println("IOException is caught");
			e.printStackTrace();
			System.exit(-1);
		}

		//ArrayList<String> listOfUsers = new ArrayList<>();
		//Reread the user list into the listOusers then add new user new to it
		//obsList =  FXCollections.observableArrayList(listOfUsers);
		//listView.setItems(obsList);
	}

	@FXML
	public void deleteUser(ActionEvent evt) throws IOException {

	}
}
