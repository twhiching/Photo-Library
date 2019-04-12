package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.view.loginController;
import sample.users.Admin;

public class Main extends Application {
	
	Stage mainStage;

    @Override
    public void start(Stage primaryStage){
    	
    	mainStage = primaryStage;
		mainStage.setTitle("Photo Library");
		
        try {
        	FXMLLoader loader = new FXMLLoader();
     		loader.setLocation(getClass().getResource("/sample//view/loginPage.fxml"));	
     		AnchorPane root = (AnchorPane)loader.load();
     		
     		loginController Controller = loader.getController();
     		Controller.start(mainStage); 
     
             primaryStage.setScene(new Scene(root, 250, 125));
             primaryStage.setResizable(false);
             primaryStage.show();
        }catch(Exception e) {
        	e.printStackTrace();
        }               
    }

    public static void main(String[] args) {
    	Admin.stockExist();
        launch(args);
    }
}
