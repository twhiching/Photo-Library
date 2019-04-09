package sample.view;

import java.io.*;
import java.util.*;

//mport javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Album;
import sample.Photo;
import sample.users.Default;

public class userController implements Serializable{

	Stage mainStage;
	
	private int index;
	
	private String userName;

	private Default user;
	
	@FXML         
	private Button logout;
	
	@FXML
	private Button addAlbum;
	
	@FXML
	private Button addPhoto;
	
	@FXML
	private Button edit;
	
	@FXML
	private Button delete;

	@FXML
	private ListView<String> listView;

	@FXML
	private ScrollPane scrollPane;
	
	@FXML
	private TilePane tilePane;
	
	@FXML
	private ImageView selectedPhoto;
	
	@FXML
	private Label photoName;
	
	@FXML
	private Label thumbnailViewText;
	
	@FXML
	private TextField nameArea;
	
	@FXML
	private TextField captionArea;
	
	@FXML
	private TextField tag1Area;
	
	@FXML
	private TextField tag2Area;
	
	@FXML
	private TextField searchBox;
	
	private ObservableList<String> obsList;
	
	public void setUserName(String name) {
		userName = name;
	}
	
	public void start(Stage primaryStage,String name) {
		
		mainStage = primaryStage;

		//Load up all albums users may have and display them in the list view
		//Load up all photo's user may have and display them in the grid view
		//Select first photo in the list to be displayed in the photo's tab

		//Set user name
		userName = name;
		System.out.println("User name is:"+userName);
		
		//Set label for thumbnail view
		thumbnailViewText.setText(userName+"'s photos");
		
		//Create the path for where the users photos are stored
        String dir = System.getProperty("user.dir");
        String PATH = dir+"/src/sample/users/";
		String directoryName = PATH.concat(userName+"/"+userName+".ser");
		System.out.println("Path to .ser file is:"+directoryName);
		
		//Deserialize the user object first
		user = null;
		
		try{    
	           // Reading the object from a file 
	           FileInputStream file = new FileInputStream(directoryName); 
	           ObjectInputStream in = new ObjectInputStream(file); 
	              
	           // Method for deserialization of object 
	           user = (Default)in.readObject(); 
	              
	           in.close(); 
	           file.close(); 
	              
	           System.out.println("Object has been deserialized ");
	           LinkedList<Photo> userPhotos = user.getPhotos();
	           
	           for(int i=0; i < userPhotos.size(); i++){
	              System.out.println("Element at index "+i+": "+userPhotos.get(i).getPhotoPath());
	            } 
	         	          
	       }catch(IOException | ClassNotFoundException ex){
	    	   ex.printStackTrace();
	           System.out.println("IOException is caught"); 
	       }
		loadUserPhotos();
		//ArrayList<String> albumList = new ArrayList<>();
		obsList =  FXCollections.observableArrayList(user.listAlbumnnames());		
		listView.setItems(obsList); 
		mainStage.setOnHiding( event -> {
			try {
				logout(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} );
	}
	
	private void loadUserPhotos() {
	     		        
		tilePane.getChildren().clear();
        LinkedList<Photo> userPhotos = user.getPhotos();
        
        for(int i=0; i < userPhotos.size(); i++){
        	FileInputStream input;
			try {
				input = new FileInputStream(userPhotos.get(i).getPhotoPath());
				Image image = new Image(input);
	            ImageView imageView = new ImageView(image);           
	            imageView.setFitWidth(88.5);
	            imageView.setFitHeight(88.5);     	            
	            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
	            	@Override
	            	public void handle(MouseEvent mouseEvent) {
	            		         	            		            		            		
	            		Node n = (Node) mouseEvent.getSource();
	            		int xPos = (int) n.getLayoutX();
	            		int yPos = (int) n.getLayoutY();	            		
	            		int count = 0;
	            		System.out.println(xPos);
	            		System.out.println(yPos);
	            		if(xPos == 15) {
	            			xPos = 0;
	            		}else {
	            			xPos = 1;	 
	            		}
	            		while(yPos != 15) {
	            			yPos = yPos - 104;
	            			++count;
	            		}
	            		
	            		//System.out.println("x is:"+xPos);
	            		//System.out.println("count is:"+count);
	            		index = xPos + (count*2);
	            		//System.out.println("index is:"+index);
	            		FileInputStream input;
						try {
							System.out.println(userPhotos.get(index).getPhotoPath());
							input = new FileInputStream(userPhotos.get(index).getPhotoPath());
							Image image = new Image(input);		            		
		            		selectedPhoto.setImage(image);
		            		photoName.setText(userPhotos.get(index).getName());
		            		nameArea.setText(userPhotos.get(index).getName());
		            		captionArea.setText(userPhotos.get(index).getCaption());
		        			tag1Area.setText(userPhotos.get(index).getTagone());
		        			tag2Area.setText(userPhotos.get(index).getTagtwo());
		            		//System.out.println("Here is the name of the photo selected:"+userPhotos.get(index).getName());
		            		
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	            		        	            	            		
	            	}
	            });
	            tilePane.getChildren().addAll(imageView);	      
	            
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	        	          
          } 
               
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        //scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);  
	}

	@FXML
	public void logout(ActionEvent evt) throws IOException {
		
		
		//TODO add a dialog popup to confirm user choice of logging out
		
		//Serialized the user object before logging out
		//Create the path for where the users photos are stored
		System.out.println("Closing the stage");
        String dir = System.getProperty("user.dir");
        String PATH = dir+"/src/sample/users/";
		String directoryName = PATH.concat(userName+"/"+userName+".ser");
		System.out.println("Path to .ser file is:"+directoryName);
		try
        {    
            //Saving of object in a file 
            FileOutputStream file = new FileOutputStream(directoryName); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
              
            // Method for serialization of object 
            out.writeObject(user); 
              
            out.close(); 
            file.close(); 
              
            System.out.println("Object has been serialized"); 
  
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught"); 
        }
		
		FXMLLoader loader = new FXMLLoader();
 		loader.setLocation(getClass().getResource("/sample//view/loginPage.fxml"));	
		AnchorPane root = (AnchorPane)loader.load();
 		
 		loginController Controller = loader.getController();
 		Controller.start(mainStage); 
 
 		mainStage.setScene(new Scene(root, 250, 125));
 		mainStage.setResizable(false);
 		mainStage.show();
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
		//Album newAlbum = new Album(result.get());
		//Rewrote logic since serialization was reworked
		if(user.duplicateAlbumcheck(result.get())) {
			System.out.println("Adding new album");
			Album newAlbum = new Album(result.get());
			user.addAlbum(newAlbum);
			System.out.println("Added album: " + result.get());
			updateListView();
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Album with the name already exists");
			alert.setContentText(result.get() + " already exists");
			alert.showAndWait();
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
		
		//TODO Check to make sure user didn't back out and not select a photo
		File selectedFile = fileChooser.showOpenDialog(mainStage);
		//After selecting a file it saves the true path in this variable
		if(selectedFile != null ) {
			String photoPath = selectedFile.toString();
			File fileOne = new File(photoPath);
			String photoNameraw = fileOne.getName();
			String photoNamecleaned = photoNameraw.substring(0, photoNameraw.lastIndexOf("."));
			System.out.println(photoNamecleaned);
			System.out.println(photoNameraw);
			System.out.println(photoPath);
	        System.out.println("Adding new photo");
			Photo userPhoto = new Photo(photoNamecleaned, photoPath);
			user.addPhoto(userPhoto);
			loadUserPhotos();
		}
		/*
		//System.out.println(photoPath);
		//Trying to extract just the file name below
		File fileOne = new File(photoPath);
		String photoNameraw = fileOne.getName();
		//String extensionRegex = "\\.[a-z] {3,4}";
		String photoNamecleaned = photoNameraw.substring(0, photoNameraw.lastIndexOf("."));
		System.out.println(photoNamecleaned);
		
		Default userAlbum = null;
		//Below will be deserialization and serialization of object
		try {
			String dir = System.getProperty("user.dir");
	        String path = dir+"/src/sample/users/" + user + "/" + user + ".ser";
			FileInputStream fileTwo = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileTwo);
			System.out.println("Deserializing user");
			user = (Default)in.readObject();
			System.out.println("Adding new photo");
			Photo userPhoto = new Photo(photoNamecleaned, photoNameraw);
			user.addPhoto(userPhoto);
			in.close();
			fileTwo.close();
			
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
		}*/
		
	}
	
	@FXML
	public void edit(ActionEvent evt) {
		//Can edit either an selected picture or album name
		//First picture
		System.out.println("I'm in edit!!!");
		LinkedList<Photo> userPhotos = user.getPhotos();
		Alert alert = new Alert(AlertType.CONFIRMATION, "Edit " + userPhotos.get(index).getName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			if(!(photoName.getText().equals(nameArea.getText()))){
				photoName.setText((nameArea.getText()));
			}
			user.getPhoto(index).setName(nameArea.getText());
			user.getPhoto(index).setCaption(captionArea.getText());
			user.getPhoto(index).setTagone(tag1Area.getText());
			user.getPhoto(index).setTagtwo(tag2Area.getText());
		}
	}
	
	@FXML
	public void delete(ActionEvent evt) {
		/*
		 * Problem is that it deletes a photo and album at the same time
		 * */
		
		//Can delete either an selected picture or album
		//First picture
		LinkedList<Photo> userPhotos = user.getPhotos();
		Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + userPhotos.get(index).getName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		alert.showAndWait();

		System.out.println(userPhotos.size());
		if (alert.getResult() == ButtonType.YES) {
			System.out.println("index is:"+index);
			System.out.println("Selected photo to delete is:"+userPhotos.get(index).getName());
			user.deletePhoto(userPhotos.get(index));
			selectedPhoto.setImage(null);
			photoName.setText(null);
			loadUserPhotos();
		}
		//Deleting album
		String item = listView.getSelectionModel().getSelectedItem();
	    int index = listView.getSelectionModel().getSelectedIndex();
	    //Make sure an item is selected before being able to delete an entry
	    if(item == null || index == -1) {    	
	    	Alert deleteAlert = new Alert(AlertType.ERROR);
	    	deleteAlert.setTitle("Error Dialog");
	    	deleteAlert.setHeaderText("Must pick an album first");
	    	deleteAlert.setContentText("An entry must be picked in order to delete a album");
	    	deleteAlert.showAndWait();
	    }else {	
	    	Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Delete " + item + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
	    	deleteAlert.showAndWait();
	    	//Delete selected item
	    	if(deleteAlert.getResult() == ButtonType.YES) {
	    		System.out.println("Deleting: " + item);
			    user.deleteAlbumindex(index);	
			    updateListView();
	    	}			    	
		    //Reset all text areas to blanks
		    photoName.setText("");
			nameArea.setText("");
			captionArea.setText("");
			tag1Area.setText("");
			tag2Area.setText("");
			listView.getSelectionModel().clearSelection();
			index = index - 1;
			if(!(index < 0)) {
				listView.getSelectionModel().select(index);
			}else {
				listView.getSelectionModel().clearSelection();
				listView.getSelectionModel().select(0);
			}
	    }			
	}
	
	@FXML
	public void onEnter(ActionEvent event) {
		System.out.println("In the search box");
		System.out.println("Searching: " + searchBox.getText());
	}
	
	private void updateListView() {
		//Simple function that updates list view for album names
		ArrayList<String> albumList = new ArrayList<>();		
		albumList = user.listAlbumnnames();	
		obsList =  FXCollections.observableArrayList(albumList);		
		listView.setItems(obsList);
	}
}
