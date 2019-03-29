package sample.users;

import sample.Album;
import sample.Photo;

import java.io.Serializable;
import java.util.LinkedList;


public class Default implements Serializable {
    private String userName;
    private LinkedList<Album> albumList = new LinkedList<Album>();
    private LinkedList<Photo> photoList = new LinkedList<Photo>();

    public Default(String name){
        userName = name;
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
}
