package sample;
/*
 * Overview:
 * Name
 * Caption
 * 2 Tags
 * Date using DateTime Object (When it was last modified)
 * Path to Photo(String)
*/
import java.lang.String;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Photo implements Serializable{

	private String name;
	private String caption;
	private String tagOne;
	private String tagTwo;
	private String photoPath;
	private String date;
	//I am not sure about this but I think we need to make a list of the albums a photo can be associated with
	//Might not stay but just adding incase
	private ArrayList<String> connectedAlbums;
	
	/* Getter Functions */
	public String getName(){
		return this.name;
	}
	public String getCaption() {
		return this.caption;
	}
	@SuppressWarnings("null")
	public String[] getTags() {
		String tags[] = null;
		tags[0] = this.tagOne;
		tags[1] = this.tagTwo;
		return tags;
	}
	public String getTagone() {
		return this.tagOne;
	}
	public String getTagtwo() {
		return this.tagTwo;
	}
	public String getDate() {
		return this.date;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public ArrayList<String> getconnectedAlbums() {
		return connectedAlbums;
	}
	
	/* Setter Functions */
	public void setName(String newName){
		this.name = newName;
	}
	public void setCaption(String newCaption) {
		this.caption = newCaption;
	}
	public void setTagone(String newTag) {
		this.tagOne = newTag;
	}
	public void setTagtwo(String newTag) {
		this.tagTwo = newTag;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public void setDate(String newDate) {
		this.date = newDate;
	}
	public void setConnectedalbums(ArrayList<String> setAlbum) {
		this.connectedAlbums = setAlbum;
	}
	
	/* Default Classes */
	public Photo(String givenName, String givenPath, String album) {
		name = givenName;
		caption = null;
		tagOne = null;
		tagTwo = null;
		setPhotoPath(givenPath);
		SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
		File checkPhoto = new File(givenPath);
		date = formatDate.format(checkPhoto.lastModified());
		connectedAlbums = new ArrayList<String>();
		addAlbumname(album);
	}
	
	public Photo(String givenName, String givenPath, String givenCaption, String givenTagOne, String givenTagTwo) {
		name = givenName;
		caption = givenCaption;
		tagOne = givenTagOne;
		tagTwo = givenTagTwo;
		setPhotoPath(givenPath);
		SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
		File checkPhoto = new File(givenPath);
		date = formatDate.format(checkPhoto.lastModified());
	}
	
	//Might need more functions below for photo
	public void addAlbumname(String addAlbum) {
		//System.out.println("PhotoAlbum name1: " + addAlbum);
		connectedAlbums.add(addAlbum);
		//remove any duplicates! this si a quick fix for the problem!
		ArrayList<String> newList = new ArrayList<String>();

		// Traverse through the first list
		for (String element : connectedAlbums) {

			// If this element is not present in newList
			// then add it
			if (!newList.contains(element)) {

				newList.add(element);
			}
		}

		connectedAlbums = newList;

		//System.out.println("PhotoAlbum name2: " + addAlbum);
	}
	public void deleteSelectedalbum(String album) {
		System.out.println("Album passed in to photo is: "+ album);
		System.out.println("Before");
		System.out.println(connectedAlbums);
		int i = getSelectedAlbum(album);
		if(i > -1){
			connectedAlbums.remove(i);
		}
		System.out.println("After");
		System.out.println(connectedAlbums);
	}
	
	public int getSelectedAlbum(String album) {
		int index = 0;
		ArrayList<String> tempAlbums = listAlbumnames();
		System.out.println("Here is the tempAlbum: "+tempAlbums);
    	for(String i : tempAlbums) {
			System.out.println("I is: " + i);
			System.out.println("connectedAlbums index is: " + tempAlbums.get(index));
			if (!(tempAlbums.get(index).equals(album))) {
				System.out.println("Index is increasing!");
				index++;
			}else{
				System.out.println("Index in the photo thing is: "+index);
				return index;
			}
		}
    	System.out.println("Index in the photo thing is -1");
    	return -1;
	}
	
	public ArrayList<String> listAlbumnames(){
    	ArrayList<String> tempAlbum = new ArrayList<String>();
    	for(int i = 0; i < connectedAlbums.size(); i++)
    		tempAlbum.add(connectedAlbums.get(i));
    	
    	return tempAlbum;
    }
	
	public Date getDateTime() {
		Date date = null;
		try {
			date = new SimpleDateFormat("MM/dd/yyyy").parse(getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		return date;
	}
}
