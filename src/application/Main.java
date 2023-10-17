package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("/fxml/TbPersonas.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass() .getResource("/css/application.css").toExternalForm());
			/*
			 * Añadimos el Icono
			 * */
			Image icono = new Image(Main.class.getResourceAsStream("/img/agenda.png"));
			primaryStage.getIcons().add(icono);
			
			primaryStage.setTitle("Personas");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
