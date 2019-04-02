package sample;

import sample.Photo;
import java.util.LinkedList;
import java.io.Serializable;

public class Album implements Serializable{
	
	private String name;
	private LinkedList<Photo> albumName;
	
	public LinkedList<Photo> getAlbumName() {
		return albumName;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	public String getName() {
		return this.name;
	}
	public Album(String name) {
		this.name = name;
		this.albumName = new LinkedList<Photo>();
	}

}
