package sample.users;

import sample.Album;
import sample.Photo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Admin implements Serializable{
	
	public static void stockExist() {
		String stockDirectory = System.getProperty("user.dir") + "/src/sample/users/stock";
        try{
            File stockUser = new File(stockDirectory + "/stock.ser");
            if(stockUser.exists()){
                //System.out.println(stockUser.getAbsolutePath());
                System.out.println("File Exists");
            }
            else {
                System.out.println("Creating stock user");
                String targetPath = stockDirectory + "/stock.ser";
                String stockPath = System.getProperty("user.dir") + "/src/sample/users/stockPhotos";
                try {
                	//System.out.println("Creating stock directory");
                	File stockFolder = new File(stockDirectory);
                	boolean success = stockFolder.mkdirs();
                	if(success){
                		//System.out.println("Directory successfully made");
	                	//Create new stock user
	                	Default stockObject = new Default("stock");
	                	Album stockAlbum = new Album("stock");
	                	stockObject.addAlbum(stockAlbum);
	                	
	                	//System.out.println("Adding stock photos");
	                	//Load Photos onto stock album
	                	stockObject.addAlbumphoto("stock", new Photo("bojack", stockPath + "/bojack.jpg",stockAlbum.getName()));
	                	stockObject.addAlbumphoto("stock", new Photo("clippy", stockPath + "/clippy.jpg",stockAlbum.getName()));
	                	stockObject.addAlbumphoto("stock", new Photo("discord", stockPath + "/discord.jpg",stockAlbum.getName()));
	                	stockObject.addAlbumphoto("stock", new Photo("gitBlame", stockPath + "/gitBlame.jpeg",stockAlbum.getName()));
	                	stockObject.addAlbumphoto("stock", new Photo("houseMD", stockPath + "/houseMD.jpg",stockAlbum.getName()));
	                	stockObject.addAlbumphoto("stock", new Photo("sesh", stockPath + "/sesh.jpg",stockAlbum.getName()));
	                	
	                	//Serializing
	                	FileOutputStream fileOut = new FileOutputStream(targetPath);
	                	ObjectOutputStream out = new ObjectOutputStream(fileOut);
	                	out.writeObject(stockObject);
	                	out.close();
	                	fileOut.close();
	                	
	                	//System.out.println("Successfully serialized Stock");
                	}else {
                		System.out.println("Failed making directory");
                	}
                }catch(Exception e) {
                	e.printStackTrace();
                }
            }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
