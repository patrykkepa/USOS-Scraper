package com.example.scraperGUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DormitoryController implements Initializable {
    @FXML
    private Button btn_back;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Button btn_getData;

    private String[] choices = {"all","2","3","5","7","8","9","10","11","13","14"};

    private void checkIfFilterChanged(){
        choiceBox.setOnAction(new EventHandler<ActionEvent>() {
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
                btn_back.setDisable(true);

                DormitoryScraper dormitoryScraper = new DormitoryScraper(choiceBox.getValue());
                dormitoryScraper.valueProperty().addListener(new ChangeListener<Integer>() {
                    @Override
                    public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                        if(t1 != 0){
                            btn_getData.setText("Done!");
                            btn_getData.setDisable(false);
                            btn_back.setDisable(false);
                        }else{
                            btn_getData.setText("Failure");
                            btn_getData.setDisable(false);
                            btn_back.setDisable(false);
                        }
                    }
                });

                Thread th = new Thread(dormitoryScraper);
                th.setDaemon(true);
                th.start();

            }
        });

        checkIfFilterChanged();
    }
}
