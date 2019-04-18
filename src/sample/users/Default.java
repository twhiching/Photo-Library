package sample.users;

import sample.Album;
import sample.Photo;

import java.io.Serializable;
import java.util.LinkedList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


@SuppressWarnings("serial")
public class Default implements Serializable {
    private String userName;
    private LinkedList<Album> albumList = new LinkedList<Album>();
    private LinkedList<Photo> photoList = new LinkedList<Photo>();

    public Default(String name){
        userName = name;
        albumList = new LinkedList<Album>();
        photoList = new LinkedList<Photo>();
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String name){
        userName = name;
    }

    //Getters for the album and photo lists
    public LinkedList<Album> getAlbums(){
        return albumList;
    }
    
    public Album getAlbum(int index){
        return albumList.get(index);
    }

    public LinkedList<Photo> getPhotos(){
        return photoList;
    }
    
    public Photo getPhoto(int index){
        return photoList.get(index);
    }
    
    public ArrayList<String> listAlbumnames(){
    	ArrayList<String> tempAlbum = new ArrayList<String>();
    	for(int i = 0; i < albumList.size(); i++)
    		tempAlbum.add(albumList.get(i).getName());
    	
    	return tempAlbum;
    }
    
    //Searches through the album linked list and returns the index of the album if it can be found
    //If the album cannot be found then return -1
    public int findAlbum(String name) {
    	int index = 0;
    	for(String i : listAlbumnames()) {
    		if(i != name)
    			index++;
    		else
    			return index;
    	}
    	return index-1;
    }

    //Methods to add new albums and photos to the users lists
    public void addAlbum(Album album){
        albumList.add(album);
    }

    public void addPhoto(Photo photo){
        photoList.add(photo);
    }
    //Targeted function to add photo given the name of the album
    public void addAlbumphoto(String albumName, Photo selectedPhoto) {
   	 	int albumIndex = findAlbum(albumName);
   	 	if(albumIndex != -1) {
   	 		if(!albumList.get(albumIndex).getAlbumPhotos().contains(selectedPhoto))
   	 			albumList.get(albumIndex).addPhoto(selectedPhoto); 	 	
   	 		else {
	   	 		Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Duplicate Photo");
				alert.setContentText("Photo already exists in album");
				alert.showAndWait();
   	 		}  	 		
   	 	}
   	 	//Then sort the photos in the album by most recent date
   	 	Album tempAlbum = albumList.get(albumIndex);
   	 	LinkedList<Photo> albumPhotos = tempAlbum.getAlbumPhotos();
   	 	
   	 	Collections.sort(albumPhotos, new Comparator<Photo>() {
   	 		public int compare(Photo o1, Photo o2) {
   	 			return o1.getDateTime().compareTo(o2.getDateTime());
   	 		}
   	 	});
   	 	albumList.get(albumIndex).setAlbumPhotos(albumPhotos);
    }

    //Methods to delete an album or photo from the user lists
    public void deleteAlbum(Album album){
        albumList.remove(album);
    }
    public void deleteAlbumindex(int index) {
    	albumList.remove(index);
    }

    public void deletePhoto(Photo photo){
        photoList.remove(photo);
    }
    public boolean duplicateAlbumcheck(String name) {
    	//Basic input validation to make sure no duplicate albums can be created
    	for(String i : listAlbumnames()) {
    		if(i.equals(name))
    			return false;
    	}
    	return true;
    }
    public boolean findDuplicatephoto(String targetPath, String targetAlbum) {
    	for(Photo p : albumList.get(findAlbum(targetAlbum)).getAlbumPhotos()) {
    		System.out.println("Album " + albumList.get(findAlbum(targetAlbum)).getName());
    		System.out.println("Photo path: " + p.getPhotoPath() + "|Selected Path: " + targetPath);
    		if(p.getPhotoPath().contentEquals(targetPath))
    			return false;
    		else
    			continue;
    	}
    	return true;
    }
}
