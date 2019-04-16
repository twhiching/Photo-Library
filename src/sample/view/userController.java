package sample.view;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.control.DatePicker;
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
	private Button slideShow;

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
	private Label date;
	
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
				e.printStackTrace();
			}
		} );
	}
	
	private void loadAlbumPhotos() {
		int albumIndex = listView.getSelectionModel().getSelectedIndex();
		isPhotoShown = false;
		//System.out.println("Album name is: "+albumIndex);
		thumbnailViewText.setText(listView.getSelectionModel().getSelectedItem()+" photo's");
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
		index = 0;
        
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
	            		//System.out.println(xPos);
	            		//System.out.println(yPos);
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
		            		//selectedPhoto.setFitWidth(220);
		            		//selectedPhoto.setFitHeight(199); 		                       		
		            		isPhotoShown = true;
		            		photoName.setText(photos.get(index).getName());
		            		date.setText(photos.get(index).getDate());
		            		nameArea.setText(photos.get(index).getName());
		            		captionArea.setText(photos.get(index).getCaption());
		        			tag1Area.setText(photos.get(index).getTagone());
		        			tag2Area.setText(photos.get(index).getTagtwo());
		            		//System.out.println("Here is the name of the photo selected:"+userPhotos.get(index).getName());
		            		
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}	            		        	            	            		
	            	}
	            });
	            tilePane.getChildren().addAll(imageView);	      
	            
			} catch (FileNotFoundException e) {
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
		//System.out.println("Closing the stage");
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
			//System.out.println("Selected Album: " + selectedAlbum);
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
				Photo userPhoto = new Photo(photoNamecleaned, photoPath,selectedAlbum);
				//Breaks when adding photo here
				//System.out.println("UserController before photo is added: " + selectedAlbum);
				//userPhoto.addAlbumname(selectedAlbum);
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
				//Search through all albums for that photo and make the change if photo matches
				Photo photo = user.getAlbum(user.findAlbum(album)).getPhoto(index);
				ArrayList<String> albumsConnectedToPhoto = photo.getconnectedAlbums();
				for(String i:albumsConnectedToPhoto) {
					System.out.println(i);
				}
				//Go through all albums asscoiated with that photo
				//System.out.println("Size of the albumsConnectedToPhoto is: "+albumsConnectedToPhoto.size());
				for(int i = 0; i < albumsConnectedToPhoto.size();++i) {
					
					LinkedList<Photo> albumPhotos = user.getAlbum(user.findAlbum(albumsConnectedToPhoto.get(i))).getAlbumPhotos();
					//System.out.println("Size of the albumPhotos is: "+albumPhotos.size());
					//Go through all photos in that album				
					for(int j = 0; j <albumPhotos.size(); ++j) {	
						//Check to see if photo paths match before making the changes!
						System.out.println("Gonna compare this: "+albumPhotos.get(j).getPhotoPath());
						System.out.println("With this: "+photoSelected.getPhotoPath());
						if(albumPhotos.get(j).getPhotoPath().equals(photoSelected.getPhotoPath())) {
							user.getAlbum(user.findAlbum(albumsConnectedToPhoto.get(i))).getPhoto(j).setName(nameArea.getText());
							user.getAlbum(user.findAlbum(albumsConnectedToPhoto.get(i))).getPhoto(j).setCaption(captionArea.getText());
							user.getAlbum(user.findAlbum(albumsConnectedToPhoto.get(i))).getPhoto(j).setTagone(tag1Area.getText());
							user.getAlbum(user.findAlbum(albumsConnectedToPhoto.get(i))).getPhoto(j).setTagtwo(tag2Area.getText());
						}
					}								
				}										
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
					//System.out.println("index is:"+index);
					//heySystem.out.println("Selected photo to delete is:"+albumPhotos.get(index).getName());
					Photo photo = user.getAlbum(albumIndex).getPhoto(index);
					user.getAlbum(albumIndex).getPhoto(index).deleteSelectedalbum(album);
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
			    		//Must first disassociate all photos with this album in all the other albums the user has
			    		System.out.println("Deleting: " + item);
			    		LinkedList<Photo> deletingAlbumPhotos = user.getAlbum(user.findAlbum(item)).getAlbumPhotos();
			    		//Now run a for loop through all the users albums and check if there are any photos in those albums that match with the photos in the deleting album
						LinkedList<Album> allUserAlbums = user.getAlbums();
						//Loop through all of the user albums
						for(int i = 0; i < allUserAlbums.size(); ++i){
							//Skip the album that's gonna be deleted
							if(allUserAlbums.get(i).getName().equals(item)){
								continue;
							}
							//Loop through all photos in the deleting album
							for(int k = 0; k < deletingAlbumPhotos.size(); ++k){
								//Loop through all photos in the non deleting album
								for(int j = 0; j < allUserAlbums.get(i).getAlbumPhotos().size(); ++j){
									System.out.println("Comparing photo from deleting album: "+ deletingAlbumPhotos.get(k).getPhotoPath());
									System.out.println("To photo from non deleting album: "+ allUserAlbums.get(i).getPhoto(j).getPhotoPath());
									if(deletingAlbumPhotos.get(k).getPhotoPath().equals(allUserAlbums.get(i).getPhoto(j).getPhotoPath())){
										System.out.println("In album: "+ user.getAlbum(i).getName()+" looking at photo: "+ user.getAlbum(i).getPhoto(j).getPhotoPath() + " disassociating this album from it: " + item);
										user.getAlbum(i).getPhoto(j).deleteSelectedalbum(item);
									}
								}
							}
						}
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
					tilePane.getChildren().clear();
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
	
	//There should be functionality to create an album containing the search results.
	@FXML
	public void onEnter(ActionEvent event) {
		//System.out.println("In the search box");
		System.out.println("Searching: " + searchBox.getText());
		String toSearch = searchBox.getText();
		//Temperary Photo object that will save all the photos found in the search result
		LinkedList<Photo> searchedPhotos = null;
		//Creating a new album called searched, check if album already exist, if yes delete all photos
		if(!user.duplicateAlbumcheck("Searched")) {
			System.out.println("This album already exists, deleting all perviously stored photos");
			int searchIndex = user.findAlbum("Searched");
			System.out.println("Index of search is: " + searchIndex);
			searchedPhotos = user.getAlbum(searchIndex).getAlbumPhotos();
			if(!searchedPhotos.isEmpty()) {
				searchedPhotos.clear();
			}
		}else {
			System.out.println("Creating new searched Album");
			searchedPhotos = new LinkedList<Photo>();
			user.addAlbum(new Album("Searched"));
		}
		Pattern firstCheck = Pattern.compile("AND|OR");
		Matcher matches = firstCheck.matcher(toSearch);
		//Single Date regex
		Pattern singleDate = Pattern.compile("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$");
		Matcher singleDateMatches = singleDate.matcher(toSearch);
		//Date range regex
		Pattern rangeDate = Pattern.compile("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}-(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$");
		Matcher rangeDateMatches = rangeDate.matcher(toSearch);
		//Triple check if value contains boolean statement, date, else false
		if(matches.find()) {
			//Example input location=paris AND weather=sunny
			System.out.println("This search result contains a boolean result");
			String[] booleanSplit = null;
			Pattern andCheck = Pattern.compile("AND");
			Matcher andMatch = andCheck.matcher(toSearch);
			if(andMatch.find()) {
				booleanSplit = toSearch.split("AND");
				//int totalAlbums = user.getAlbums().size();
				//System.out.println("Number of albums: " + totalAlbums);
				//Iterate through all albums->photos to check if true
				for(Album a : user.getAlbums()) {
					for(Photo p : a.getAlbumPhotos()) {
						System.out.println("Tag one: " + p.getTagone() + " | Tag two: " + p.getTagtwo());
						//System.out.println("Boolean tags: " + booleanSplit[0] + " | " + booleanSplit[1]);
						/*if(p.getTagone().toString().equals(booleanSplit[0]) && p.getTagtwo().toString().equals(booleanSplit[1])
							|| p.getTagone().toString().equals(booleanSplit[1]) && p.getTagtwo().toString().equals(booleanSplit[0])) {
							System.out.println("Photo matched: " + p.getName());
							int pIndex = p.getSelectedalbum("Searched");
							//user.getAlbum(pIndex);
							Photo tempPhoto = new Photo(p.getName(), p.getPhotoPath(), user.getAlbums().get(pIndex).getName());
							//Add a check to make sure no two of the same photos are added
							if(!searchedPhotos.contains(tempPhoto)) {
								searchedPhotos.add(new Photo(p.getName(), p.getPhotoPath(), user.getAlbums().get(pIndex).getName()));
							}else
								System.out.println("Denied Attempting to add redudenct photo");
						}*/
					}
				}
			}else {
				booleanSplit = toSearch.split("OR");
				for(Album a: user.getAlbums()) {
					for(Photo p : a.getAlbumPhotos()) {
						if(p.getTagone().toString().equals(booleanSplit[0]) || p.getTagtwo().toString().equals(booleanSplit[1])) {
							System.out.println("Photo matched: " + p.getName());
							int pIndex = p.getSelectedAlbum("Searched");
							Photo tempPhoto = new Photo(p.getName(), p.getPhotoPath(), user.getAlbums().get(pIndex).getName());
							if(!searchedPhotos.contains(tempPhoto)) {
								searchedPhotos.add(new Photo(p.getName(), p.getPhotoPath(), user.getAlbums().get(pIndex).getName()));
							}else
								System.out.println("Denied Attempting to add redudenct photo");
						}
					}
				}
			}
		}
		//Date range (MM/dd/yyyy-MM/dd/yyyy)
		//Need a better way to see if it is a date
		else if(rangeDateMatches.find()) {
			System.out.println("This is a time range period result");
			//Converting string to date format
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			//Split date into two different string array indexes
			try {
				String[] dateSplit = toSearch.split("-");
				Date dateStart = df.parse(dateSplit[0]);
				Date dateEnd = df.parse(dateSplit[1]);
				System.out.println("Date Start: " + dateStart + " | date End: " + dateEnd);
				//check if photo is within a certain date range
				for(Album a : user.getAlbums()) {
					for(Photo p : a.getAlbumPhotos()) {
						Date photoDate = df.parse(p.getDate());
						if(photoDate.after(dateStart) && photoDate.before(dateEnd)) {
							//Check for duplicate
							Photo tempPhoto = p;
							if(!searchedPhotos.contains(tempPhoto)) {
								p.getconnectedAlbums().add("Searched");
								searchedPhotos.add(p);
							}else {
								System.out.println("Invalid attempt to add a photo");
							}
						}else {
							System.out.println("Error in date range class");
						}
					}
				}
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		//Single date value
		else if(singleDateMatches.find()){
			System.out.println("In Single Date Search");
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			try {
				Date targetDate = df.parse(toSearch);
				System.out.println("Pasred Target Date: " + targetDate);
				for(Album a : user.getAlbums()) {
					for(Photo p : a.getAlbumPhotos()) {
						Date photoDate = df.parse(p.getDate());
						if(photoDate.equals(targetDate)) {
							Photo tempPhoto = p;
							if(!searchedPhotos.contains(tempPhoto)) {
								p.getconnectedAlbums().add("Searched");
								searchedPhotos.add(p);
							}else {
								System.out.println("Invalid attempt to add a photo");
							}
						}else {
							System.out.println("Error in date range class");
						}
					}
				}
			}catch(ParseException e){
				e.printStackTrace();
			}
		}
		//key=value pair
		else if(toSearch.contains("=")) {
			System.out.println("One search query");
			//You are searching by key=value pair
			for(Album a : user.getAlbums()) {
				for(Photo p : a.getAlbumPhotos()) {
					System.out.println("Photo " + p.getName() + " has " + p.getTagone() + " | " + p.getTagtwo());
					//If both tags are not null
					if(p.getTagone() != null && p.getTagtwo() != null) {
						System.out.println("These two values are not null");
						if(p.getTagone().equals(toSearch)) {
							//Check for duplicate
							Photo tempPhoto = p;
							if(!searchedPhotos.contains(tempPhoto)) {
								p.getconnectedAlbums().add("Searched");
								searchedPhotos.add(p);
							}else {
								System.out.println("Invalid attempt to add a photo");
							}
						}
					}else if(p.getTagone() == null && p.getTagtwo() != null) {
						if(p.getTagone().equals(toSearch)) {
							//Check for duplicate
							Photo tempPhoto = p;
							if(!searchedPhotos.contains(tempPhoto)) {
								p.getconnectedAlbums().add("Searched");
								searchedPhotos.add(p);
							}else {
								System.out.println("Invalid attempt to add a photo");
							}
						}
					//Tag two is null
					}else if(p.getTagone() != null && p.getTagtwo() == null) {
						if(p.getTagone().equals(toSearch)) {
							//Check for duplicate
							Photo tempPhoto = p;
							if(!searchedPhotos.contains(tempPhoto)) {
								p.getconnectedAlbums().add("Searched");
								searchedPhotos.add(p);
							}else {
								System.out.println("Invalid attempt to add a photo");
							}
						}
					}else
						continue;
				}
			}
		}else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Invalid Search Term");
			alert.setContentText("Please enter a valid search term");
			alert.showAndWait();
		}
		listView.getSelectionModel().clearSelection();
		searchBox.setText("");
		loadUserPhotos(searchedPhotos);
		updateListView();
	}
	
	@FXML
	public void movePhoto(ActionEvent event) {
		//Not sure why thios place throws an excpetion, if moveBox.getSelectionModel().clearSelection(); is removed the exception is no longer thrown.
		String destinationAlbum = moveBox.getSelectionModel().getSelectedItem().toString();
		String sourceAlbum = listView.getSelectionModel().getSelectedItem();
		Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Move photo: " + photoSelected.getName() + " to album: " +destinationAlbum + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    	deleteAlert.showAndWait();
    	if(deleteAlert.getResult() == ButtonType.YES) {
    		
    		System.out.println("Move Selected: " + destinationAlbum);		
    		user.addAlbumphoto(destinationAlbum, photoSelected);
    		//user.getAlbum(user.findAlbum(sourceAlbum)).deletePhoto(photoSelected);
    		photoSelected.addAlbumname(destinationAlbum);
    		user.getAlbum(user.findAlbum(sourceAlbum)).getPhoto(index).deleteSelectedalbum(sourceAlbum);
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
	}
	
	@FXML
	public void copyPhoto(ActionEvent event) {
		//Not sure why thios place throws an excpetion, if copyBox.getSelectionModel().clearSelection(); is removed the exception is no longer thrown.
		String selectedAlbum = copyBox.getSelectionModel().getSelectedItem().toString();
		Alert deleteAlert = new Alert(AlertType.CONFIRMATION, "Copy photo: " + photoSelected.getName() + " to album: " +selectedAlbum + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    	deleteAlert.showAndWait();
    	if(deleteAlert.getResult() == ButtonType.YES) {
    		//int index = copyBox.getSelectionModel().getSelectedIndex();
    		System.out.println("Copy Selected: " + selectedAlbum);
    		//Need to add this instance of the album to al photos through the user's same photo
			LinkedList<Album> userAlbums = user.getAlbums();
			LinkedList<Photo> albumPhotos;

			for(int i = 0; i < userAlbums.size();++i){
				albumPhotos = userAlbums.get(i).getAlbumPhotos();
				if(userAlbums.get(i).getName().equals(selectedAlbum)){
					photoSelected.addAlbumname(selectedAlbum);
					user.addAlbumphoto(selectedAlbum, photoSelected);
					continue;
				}else if(userAlbums.get(i).getName().equals(listView.getSelectionModel().getSelectedItem())){
					System.out.println("Item is: "+ listView.getSelectionModel().getSelectedItem());
				}
				for(int j = 0; j < albumPhotos.size();++j){
					System.out.println("ALbum comparing is: "+ userAlbums.get(i).getName());
					if(albumPhotos.get(j).getPhotoPath().equals(photoSelected.getPhotoPath())){
						System.out.println("This is the comparing photo: " + albumPhotos.get(j).getPhotoPath());
						System.out.println("This is the selected photo: " + photoSelected.getPhotoPath());
						user.getAlbum(user.findAlbum(userAlbums.get(i).getName())).getPhoto(j).addAlbumname(selectedAlbum);
					}
				}
			}

    		System.out.println("Using Photo class successfully copied over");
    		System.out.println("In the user class adding album photo");
    		try {
    			copyBox.getSelectionModel().clearSelection();
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
    	}	
	}
	
	@FXML
	public void slideShow(ActionEvent evt) {
	
		FXMLLoader loader = new FXMLLoader();
 		loader.setLocation(getClass().getResource("/sample//view/slideShowPage.fxml"));	
		AnchorPane root;
		//Make sure that an album is selected
		String selectedAlbum = listView.getSelectionModel().getSelectedItem();
		if(selectedAlbum == null) {
	    	Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Album not selected");
			alert.setContentText("An album must be picked to to view a slide Show of its pictures!");
			alert.showAndWait();
		}else if (user.getAlbum(user.findAlbum(selectedAlbum)).getAlbumPhotos().size() == 0){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("Album has no photos");
			alert.setContentText("An album must have photos in order to view a slide show of it!");
			alert.showAndWait();
		}else {
			
			//Save everything the users has done up to this point
			String dir = System.getProperty("user.dir");
	        String PATH = dir+"/src/sample/users/";
			String directoryName = PATH.concat(userName+"/"+userName+".ser");
			//System.out.println("Path to .ser file is:"+directoryName);
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
		
			try {
				Album album = user.getAlbum(user.findAlbum(selectedAlbum));
				root = (AnchorPane)loader.load();
				slideShowController Controller = loader.getController();
		 		Controller.start(mainStage, userName, index, album); 
		 		mainStage.setScene(new Scene(root, 1000, 750));
		 		mainStage.setResizable(false);
		 		mainStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//Resume state once user comes back from slide show view
	public void resumeState(Stage stage,String userName,Album album,int photoIndex) {
		start(stage,userName);
		Photo photo = album.getPhoto(photoIndex);
		listView.getSelectionModel().select(album.getName());
		try {
			FileInputStream input = new FileInputStream(photo.getPhotoPath());
			//Keep a global copy of the photo on hand for your move and copy functions
			photoSelected = photo;
			Image image = new Image(input);		            		
    		selectedPhoto.setImage(image);	                       		
    		isPhotoShown = true;
    		photoName.setText(photo.getName());
    		date.setText(photo.getDate());
    		nameArea.setText(photo.getName());
    		captionArea.setText(photo.getCaption());
			tag1Area.setText(photo.getTagone());
			tag2Area.setText(photo.getTagtwo());   		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
