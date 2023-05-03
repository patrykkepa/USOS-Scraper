package com.example.scraperGUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Button btn_dormitories;
    @FXML
    private Button btn_fieldsOfStudy;
    @FXML
    private Button btn_programs;


    @FXML
    protected void onDormitoriesButtonClick() {}

    @Override
    public void initialize(URL location, ResourceBundle resources){
//        Parent root = null;

        btn_dormitories.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.changeScene(event, "dormitory.fxml", "Dormitory");
            }
        });
        btn_programs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.changeScene(event, "programs.fxml", "Programs");
            }
        });
//        btn_fieldsOfStudy.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                Utils.changeScene(event, "fields-of-study.fxml", "Fields of study");
//            }
//        });
    }
}