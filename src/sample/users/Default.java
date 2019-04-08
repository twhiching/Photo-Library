package sample.users;

import sample.Album;
import sample.Photo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ArrayList;


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

    public LinkedList<Photo> getPhotos(){
        return photoList;
    }
    
    public Photo getPhoto(int index){
        return photoList.get(index);
    }
    
    public ArrayList<String> listAlbumnnames(){
    	ArrayList<String> tempAlbum = new ArrayList<String>();
    	for(int i = 0; i < albumList.size(); i++)
    		tempAlbum.add(albumList.get(i).getName());
    	
    	return tempAlbum;
    }

    //Methods to add new albums and photos to the users lists
    public void addAlbum(Album album){
        albumList.add(album);
    }

    public void addPhoto(Photo photo){
        photoList.add(photo);
    }

    //Methods to delete an album or photo from the user lists
    public void deleteAlbum(Album album){
        albumList.remove(album);
    }

    public void deletePhoto(Photo photo){
        photoList.remove(photo);
    }
    public boolean duplicateAlbumcheck(String name) {
    	//Basic input validation to make sure no duplicate albums can be created
    	for(String i : listAlbumnnames()) {
    		if(i.equals(name))
    			return false;
    	}
    	return true;
    	
    }
}
