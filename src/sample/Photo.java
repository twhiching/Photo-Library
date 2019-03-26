package sample;
/*
 * Overview:
 * Name
 * Caption
 * 2 Tags
 * Date (??Just display date or do they want date and time)
*/
import java.lang.String;
import java.util.Calendar;
import java.util.Date;
import java.io.Serializable;

public class Photo {

	private String name;
	private String caption;
	private String tagOne;
	private String tagTwo;
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
	public Photo(String givenName) {
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.MILLISECOND,0);
		name = givenName;
		caption = null;
		tagOne = null;
		tagTwo = null;
		date = "00/00/0000";
	}
}