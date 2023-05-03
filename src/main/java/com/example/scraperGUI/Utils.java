package com.example.scraperGUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {

    public static void changeScene(ActionEvent event, String fxmlFile, String title) {
        Parent root = null;

        try{
            FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlFile));
            root = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
