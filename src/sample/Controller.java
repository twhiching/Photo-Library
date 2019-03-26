package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller {
	
	Stage mainstage;
	@FXML         
	private Button Login;
	
	public void start(Stage stage) {
		
		mainstage = stage;
	}
}