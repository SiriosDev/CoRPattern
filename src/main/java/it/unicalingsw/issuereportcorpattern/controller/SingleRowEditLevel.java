package it.unicalingsw.issuereportcorpattern.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SingleRowEditLevel implements Initializable {
    @FXML
    private TextField nameTextField;
    @FXML
    private Spinner<Integer> levelSpinner;
    @FXML
    private Button resetButton;
    @FXML
    private Button deleteButton;
    @FXML
    private HBox base;
    @FXML
    private Group helperGroup;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        deleteButton.setOnAction(event -> {
            if (base.getParent() instanceof javafx.scene.layout.VBox) {
                ((javafx.scene.layout.Pane) base.getParent()).getChildren().remove(base);
            }
        });

        resetButton.setDisable(true);

        levelSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 0));



    }
}
