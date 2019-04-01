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
import java.util.Calendar;
import java.util.Date;
import java.io.*;
import java.text.SimpleDateFormat;

public class Photo implements Serializable{

	private String name;
	private String caption;
	private String tagOne;
	private String tagTwo;
	private String photoPath;
	private String date;
	
	
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
	//Just setting as string temporarily until I understand what date format is needed
	public void setDate(String newDate) {
		this.date = newDate;
	}
	
	/* Default Classes */
	public Photo(String givenName, String givenPath) {
		name = givenName;
		caption = null;
		tagOne = null;
		tagTwo = null;
		photoPath = givenPath;
		SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy|HH:MM:SS");
		File checkPhoto = new File(givenPath);
		date = formatDate.format(checkPhoto.lastModified());
	}
	
	public Photo(String givenName, String givenPath, String givenCaption, String givenTagOne, String givenTagTwo) {
		name = givenName;
		caption = givenCaption;
		tagOne = givenTagOne;
		tagTwo = givenTagTwo;
		photoPath = givenPath;
		SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy|HH:MM:SS");
		File checkPhoto = new File(givenPath);
		date = formatDate.format(checkPhoto.lastModified());
	}
	
	//Serializing Photo
	public boolean serializeFile(String photoName, Photo toSerialize) throws FileNotFoundException{
		File serialPhoto = new File(photoName + ".ser");
		if(!serialPhoto.exists()){
			try {
				System.out.println("Attempting to Serialize Photo");
				FileOutputStream fileName = new FileOutputStream(photoName + ".ser");
				ObjectOutputStream out = new ObjectOutputStream(fileName);
				out.writeObject(toSerialize);
				System.out.println("Successfully Serialized Photo");
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}
	
	//Deserializing Photo
	public Photo deSeralizePhoto(String serializedFile) throws FileNotFoundException, ClassNotFoundException {
		Photo tempPhoto = null;
		FileInputStream file;
		try {
			file = new FileInputStream(serializedFile);
			ObjectInputStream in = new ObjectInputStream(file);
			
			tempPhoto = (Photo)in.readObject();
			in.close();
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempPhoto;
	}
}
