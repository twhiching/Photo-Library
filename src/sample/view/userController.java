package sample.view;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	private boolean isPhotoShown = false;
	
	private Photo photoSelected;
	
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
	private ComboBox<String> moveBox;
	
	@FXML
	private ComboBox<String> copyBox;

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
	    	         	          
	       }catch(IOException | ClassNotFoundException ex){
	    	   ex.printStackTrace();
	           System.out.println("IOException is caught"); 
	       }
		obsList =  FXCollections.observableArrayList(user.listAlbumnames());	
		listView.setItems(obsList);
		listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) ->loadAlbumPhotos());
		moveBox.setItems(obsList);
		copyBox.setItems(obsList);
		mainStage.setOnHiding( event -> {
			try {
				logout(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} );
	}
	
	private void loadAlbumPhotos() {
		int albumIndex = listView.getSelectionModel().getSelectedIndex();
		isPhotoShown = false;
		System.out.println("Album name is: "+albumIndex);
		if(albumIndex > -1) {
			LinkedList<Photo> userPhotos = user.getAlbum(albumIndex).getAlbumPhotos();
			loadUserPhotos(userPhotos);
		}
		
	}

	private void loadUserPhotos(LinkedList<Photo> photos) {
	     		
		//Clear all elements of the photos
		tilePane.getChildren().clear();
		selectedPhoto.setImage(null);
		photoName.setText("");
		nameArea.setText("");
		captionArea.setText("");
		tag1Area.setText("");
		tag2Area.setText("");
        
        for(int i=0; i < photos.size(); i++){
        	FileInputStream input;
			try {
				/*for(int z = 0; z <  photos.size();z++) {
					System.out.println(photos.get(z).getName());
				}*/
				input = new FileInputStream(photos.get(i).getPhotoPath());
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
							System.out.println(photos.get(index).getPhotoPath());
							input = new FileInputStream(photos.get(index).getPhotoPath());
							//Keep a global copy of the photo on hand for your move and copy functions
							photoSelected = photos.get(index);
							Image image = new Image(input);		            		
		            		selectedPhoto.setImage(image);
		            		isPhotoShown = true;
		            		photoName.setText(photos.get(index).getName());
		            		nameArea.setText(photos.get(index).getName());
		            		captionArea.setText(photos.get(index).getCaption());
		        			tag1Area.setText(photos.get(index).getTagone());
		        			tag2Area.setText(photos.get(index).getTagtwo());
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
		try{    
            //Saving of object in a file 
            FileOutputStream file = new FileOutputStream(directoryName); 
            ObjectOutputStream out = new ObjectOutputStream(file); 
              
            // Method for serialization of object 
            out.writeObject(user);        
            out.close(); 
            file.close();              
            System.out.println("Object has been serialized"); 
        } 
          
        catch(IOException ex) { 
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
		//Make sure an album is selected
		String selectedAlbum = listView.getSelectionModel().getSelectedItem();
		if(selectedAlbum == null) {
	    	Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Album");
			alert.setContentText("An album must be picked to add a photo");
			alert.showAndWait();
		}else {
			System.out.println("Selected Album: " + selectedAlbum);
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
				//Get information on selected Album
				String photoPath = selectedFile.toString();
				File fileOne = new File(photoPath);
				String photoNameraw = fileOne.getName();
				String photoNamecleaned = photoNameraw.substring(0, photoNameraw.lastIndexOf("."));
				System.out.println("Cleaned: " + photoNamecleaned);
				System.out.println("Raw: " + photoNameraw);
				System.out.println("Full Path: " +photoPath);
		        System.out.println("Adding new photo");
				Photo userPhoto = new Photo(photoNamecleaned, photoPath);
				user.addAlbumphoto(selectedAlbum, userPhoto);
				LinkedList<Photo> Photos = user.getAlbum(user.findAlbum(selectedAlbum)).getAlbumPhotos();
				loadUserPhotos(Photos);
			}
		}
	}
	
	@FXML
	public void edit(ActionEvent evt) {
		//Can edit either an selected picture or album name
		String album = listView.getSelectionModel().getSelectedItem();
		if(isPhotoShown == true && album != null) { //This means the user wants to edit a photo from a given album
			LinkedList<Photo> Photos = user.getAlbum(user.findAlbum(album)).getAlbumPhotos();
			Alert alert = new Alert(AlertType.CONFIRMATION, "Edit " + Photos.get(index).getName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				if(!(photoName.getText().equals(nameArea.getText()))){
					photoName.setText((nameArea.getText()));
				}
				user.getAlbum(user.findAlbum(album)).getPhoto(index).setName(nameArea.getText());
				user.getAlbum(user.findAlbum(album)).getPhoto(index).setCaption(captionArea.getText());
				user.getAlbum(user.findAlbum(album)).getPhoto(index).setTagone(tag1Area.getText());
				user.getAlbum(user.findAlbum(album)).getPhoto(index).setTagtwo(tag2Area.getText());
			}
		}else if(isPhotoShown == false && album != null) { //This means the user wants to edit an album's name
			
			//Pop up dialog to get user name for the user object
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Edit Album's Name");
			dialog.setHeaderText("Enter New Album Name:");
			dialog.setContentText("Album:");
			Optional<String> result = dialog.showAndWait();
			try {
				user.getAlbum(user.findAlbum(album)).setName(result.get());
				updateListView();
			}catch(NoSuchElementException e) {
				
			}
						
		}
		else { //Nothing is selected so it is an invalid operation
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Must select an item in order to edit");
			alert.setContentText("An item must be selected in order for changes to be made to it!");
			alert.showAndWait();
		}
	}
	
	@FXML
	public void delete(ActionEvent evt) {
		
		//Can delete either an selected picture or album
		String album = listView.getSelectionModel().getSelectedItem();
		
		if(album == null) {
	    	Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Album");
			alert.setContentText("An album must be picked to delete a photo from it");
			alert.showAndWait();
		}else {
			if(isPhotoShown == true && album != null) { //This means the user wants to delete a photo from a given album
				int albumIndex = user.findAlbum(album);
				LinkedList<Photo> albumPhotos = user.getAlbum(albumIndex).getAlbumPhotos();
				Alert alert = new Alert(AlertType.CONFIRMATION, "Delete " + albumPhotos.get(index).getName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
				alert.showAndWait();

				System.out.println(albumPhotos.size());
				if (alert.getResult() == ButtonType.YES) {
					System.out.println("index is:"+index);
					System.out.println("Selected photo to delete is:"+albumPhotos.get(index).getName());
					user.getAlbum(albumIndex).deletePhoto(albumPhotos.get(index));
					//user.deletePhoto(albumPhotos.get(index));
					selectedPhoto.setImage(null);
					photoName.setText(null);
					LinkedList<Photo> updatedPhotos = user.getAlbum(albumIndex).getAlbumPhotos();
					loadUserPhotos(updatedPhotos);
				}
			}else if(isPhotoShown == false && album != null) { //This means the user wants to delete an album
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
			}else { //Nothing is selected so it is an invalid operation
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Must select an item for deletion");
				alert.setContentText("An item must be selected in order for it to be deleted!");
				alert.showAndWait();
			}
		}	
	}
	
	@FXML
	public void onEnter(ActionEvent event) {
		System.out.println("In the search box");
		System.out.println("Searching: " + searchBox.getText());
		String toSearch = searchBox.getText();
		/*
		if(searchBox.getText().equals("all photos")) {
			LinkedList<Photo> userPhotos = user.getPhotos();
			loadUserPhotos(userPhotos);
		}
		*/
		//Triple check if value contains boolean statement, date, else false
		if(toSearch.matches("AND|OR")) {
			System.out.println("This search result contains a boolean result");
		}
		//Date range (MM/dd/yyyy-MM/dd/yyyy)
		else if(toSearch.length() == 21) {
			System.out.println("This is a time period result");
			
			//Converting string to date format
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			//Some function to grade all photos date below
		}else
			System.out.println("Invalid Search Query");
		listView.getSelectionModel().clearSelection();
		searchBox.setText("");
		
	}
	
	@FXML
	public void movePhoto(ActionEvent event) {
		//Not sure why thios place throws an excpetion, if moveBox.getSelectionModel().clearSelection(); is removed the exception is no longer thrown.
		//TODO add popup box to confirm user choice
		String destinationAlbum = moveBox.getSelectionModel().getSelectedItem().toString();
		String sourceAlbum = listView.getSelectionModel().getSelectedItem();
		System.out.println("Move Selected: " + destinationAlbum);		
		user.addAlbumphoto(destinationAlbum, photoSelected);
		user.getAlbum(user.findAlbum(sourceAlbum)).deletePhoto(photoSelected);
		selectedPhoto.setImage(null);
		photoName.setText(null);
		LinkedList<Photo> updatedPhotos = user.getAlbum(user.findAlbum(sourceAlbum)).getAlbumPhotos();
		loadUserPhotos(updatedPhotos);
		try {
			moveBox.getSelectionModel().clearSelection();
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	@FXML
	public void copyPhoto(ActionEvent event) {
		//Not sure why thios place throws an excpetion, if copyBox.getSelectionModel().clearSelection(); is removed the exception is no longer thrown.
		//TODO add popup box to confirm user choice
		String selectedAlbum = copyBox.getSelectionModel().getSelectedItem().toString();
		//int index = copyBox.getSelectionModel().getSelectedIndex();
		System.out.println("Copy Selected: " + selectedAlbum);
		user.addAlbumphoto(selectedAlbum, photoSelected);
		try {
			copyBox.getSelectionModel().clearSelection();
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	private void updateListView() {
		//Simple function that updates list view for album names
		ArrayList<String> albumList = new ArrayList<>();		
		albumList = user.listAlbumnames();	
		obsList =  FXCollections.observableArrayList(albumList);		
		listView.setItems(obsList);
		moveBox.setItems(obsList);
		copyBox.setItems(obsList);
	}
}
