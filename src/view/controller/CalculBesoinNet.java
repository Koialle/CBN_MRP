package view.controller;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Class CBN_MRP
 * @author Mélanie DUBREUIL
 * @author Ophélie EOUZAN
 * 
 */
public class CalculBesoinNet extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene startScene = new Scene(root, 400, 300); 
        
        primaryStage.setTitle("CBN - MRP");
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            System.exit(0); // Close all process
        });
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
}
