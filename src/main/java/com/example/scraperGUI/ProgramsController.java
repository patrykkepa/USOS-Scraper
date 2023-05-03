package com.example.scraperGUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgramsController implements Initializable {
    @FXML
    private Button btn_back;

    @FXML
    private Button btn_getData;

    @FXML
    private TextField textField;

    @FXML
    private CheckBox chb_stacjo;

    @FXML
    private CheckBox chb_niestacjo;

    @FXML
    private ChoiceBox<String> choiceBox;

    private String[] choices = {"all","undergraduate","master's degree"};

    @FXML
    private Label label_waiting;

    public void checkIfFiltersChanged(){
        choiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                btn_getData.setText("GET DATA");
            }
        });
        chb_stacjo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                btn_getData.setText("GET DATA");
            }
        });
        chb_niestacjo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                btn_getData.setText("GET DATA");
            }
        });
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                btn_getData.setText("GET DATA");
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

//      back button setup
        btn_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utils.changeScene(event, "hello-view.fxml", "USOS Scraper");
            }
        });

//      choice box setup
        choiceBox.getItems().addAll(choices);
        choiceBox.setValue(choices[0]);

//      get data on click
        btn_getData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                btn_getData.setText("...");
                btn_getData.setDisable(true);
                label_waiting.setText("It can take a few minutes.");
                btn_back.setDisable(true);

                ProgramsScraper programsScraper = new ProgramsScraper(textField.getText(), chb_stacjo.isSelected(), chb_niestacjo.isSelected(), choiceBox.getValue());
                programsScraper.valueProperty().addListener(new ChangeListener<Integer>() {
                    @Override
                    public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                        if(t1 != 0){
                            btn_getData.setText("Done!");
                            btn_getData.setDisable(false);
                            label_waiting.setText("");
                            btn_back.setDisable(false);
                        }else{
                            btn_getData.setText("Failure");
                            btn_getData.setDisable(false);
                            label_waiting.setText("");
                            btn_back.setDisable(false);
                        }
                    }
                });
                Thread th = new Thread(programsScraper);
                th.setDaemon(true);
                th.start();


            }
        });


        checkIfFiltersChanged();
    }
}
