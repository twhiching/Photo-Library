package sample;

import sample.Photo;
import java.util.LinkedList;
import java.util.ArrayList;
import java.io.Serializable;

public class Album implements Serializable{
	
	private String name;
	private LinkedList<Photo> photos;
	
	public LinkedList<Photo> getAlbumPhotos() {
		return photos;
	}
	
	public void setAlbumPhotos(LinkedList<Photo> photos) {
		this.photos = photos; 
	}
	
	public void deletePhoto(Photo photo) {
		photos.remove(photo);
	}
	
	public Photo getPhoto(int index) {
		return photos.get(index);
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	public String getName() {
		return this.name;
	}	
	public void addPhoto(Photo selectedPhoto) {
		photos.add(selectedPhoto);
	}
	public ArrayList<String> listPhotonames(){
		ArrayList<String> tempPhotos = new ArrayList<String>();
		for(int i = 0; i < photos.size(); i++)
			tempPhotos.add(photos.get(i).getName());
		
		return tempPhotos;
	}
	public Album(String name) {
		this.name = name;
		this.photos = new LinkedList<Photo>();
	}
}
