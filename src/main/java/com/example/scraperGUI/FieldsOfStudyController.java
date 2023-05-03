package com.example.scraperGUI;

import backendd.appFieldsOfStudy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ResourceBundle;

public class FieldsOfStudyController implements Initializable {
    @FXML
    private Button btn_back;

    @FXML
    private Button btn_getData;

    @FXML
    private TextField textField;

    @Override
    public void initialize(URL location, ResourceBundle resources){
//        Parent root = null;

        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.changeScene(event, "hello-view.fxml", "USOS Scraper");
            }
        });

        btn_getData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println(textField.getText());

                int scrapedDataAmount;
                try {
                    scrapedDataAmount = appFieldsOfStudy.run(textField.getText());
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
                if(scrapedDataAmount != 0){
                    btn_getData.setText("Done!");
                    textField.setText("");
                } else {
                    btn_getData.setText("Failure");
                }


            }
        });
    }
}
