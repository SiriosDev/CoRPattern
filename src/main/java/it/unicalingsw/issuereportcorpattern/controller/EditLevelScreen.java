package it.unicalingsw.issuereportcorpattern.controller;

import it.unicalingsw.issuereportcorpattern.MainApp;
import it.unicalingsw.issuereportcorpattern.model.Database;
import it.unicalingsw.issuereportcorpattern.model.IssueType;
import it.unicalingsw.issuereportcorpattern.model.MsgBox;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class EditLevelScreen implements Initializable {
    @FXML
    private VBox vbox;
    @FXML
    private Button saveButton;
    @FXML
    private Button addButton;
    @FXML
    private AnchorPane anchorPane;

    public Boolean saved = true;

    private Boolean checkHelper(List<Integer> levels, List<String> names) {
        Set<Integer> setLevels = new HashSet<>(levels);
        Boolean disableBySpinner = setLevels.size() < levels.size();
        Boolean disableByNameTextField = names.contains("") || names.contains(null);
        return disableBySpinner || disableByNameTextField;

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        vbox.getChildren().addListener((ListChangeListener<? super Node>) c -> {
            List<Integer> levels = new ArrayList<>();
            List<HBox> bases = new ArrayList<>();
            List<String> names = new ArrayList<>();

            for (Node node : vbox.getChildren()) {
                if (node instanceof HBox) {
                    Spinner<Integer> spinner = (Spinner<Integer>) node.lookup("#levelSpinner");
                    TextField nameTextField = (TextField) node.lookup("#nameTextField");
                    bases.add((HBox) node);
                    levels.add(spinner.getValue());
                    names.add(nameTextField.getText());
                }
            }

            bases.forEach(base -> {
                Spinner<Integer> spinner = (Spinner<Integer>) base.lookup("#levelSpinner");
                TextField nameTextField = (TextField) base.lookup("#nameTextField");

                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    levels.set(bases.indexOf(base), newValue);
                    saveButton.setDisable(checkHelper(levels, names));
                    saved = false;

                });


                nameTextField.textProperty().addListener((obs, oldValue, newValue) -> {
                    names.set(bases.indexOf(base), newValue);
                    saveButton.setDisable(checkHelper(levels, names));
                    saved = false;
                });


                saveButton.setDisable(checkHelper(levels, names));
                saved = false;

            });

        });


        try {
            ObservableList<IssueType> issueTypes = Database.getAllIssueTypes();

            for (IssueType issueType : issueTypes) {
                if (issueType != null) {
                    Node singleRow = FXMLLoader.load(MainApp.class.getResource("SingleRowEditLevel.fxml"));
                    TextField nameTextField = (TextField) singleRow.lookup("#nameTextField");
                    Spinner<Integer> levelSpinner = (Spinner<Integer>) singleRow.lookup("#levelSpinner");
                    Button resetButton = (Button) singleRow.lookup("#resetButton");
                    Button deleteButton = (Button) singleRow.lookup("#deleteButton");
                    Group helperGroup = (Group) singleRow.lookup("#helperGroup");

                    helperGroup.setManaged(false);
                    helperGroup.getProperties().put("issueType", issueType);

                    nameTextField.setText(issueType.getName());
                    nameTextField.setDisable(issueType.getDefaultState());

                    deleteButton.setDisable(issueType.getDefaultState());
                    levelSpinner.getValueFactory().setValue(issueType.getLevel());



                    resetButton.setDisable(issueType.getLevel() == issueType.getDefaultLevel() || !issueType.getDefaultState());
                    if (issueType.getDefaultState())
                        resetButton.disableProperty().bind(levelSpinner.valueProperty().isEqualTo(issueType.getDefaultLevel()));
                    resetButton.setOnAction(actionEvent -> levelSpinner.getValueFactory().setValue(issueType.getDefaultLevel()));

                    vbox.getChildren().add(vbox.getChildren().size()-1, singleRow);
                }
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

        saved = true;

        addButton.setOnAction(actionEvent -> {
            try {
                Node singleRow = FXMLLoader.load(MainApp.class.getResource("SingleRowEditLevel.fxml"));

                vbox.getChildren().add(vbox.getChildren().size()-1, singleRow);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        saveButton.setOnAction(actionEvent -> {
            ObservableList<IssueType> issueTypesToSave = FXCollections.observableArrayList();

            for (Node node : vbox.getChildren()) {
                if (node instanceof HBox) {
                    TextField nameTextField = (TextField) node.lookup("#nameTextField");
                    Spinner<Integer> levelSpinner = (Spinner<Integer>) node.lookup("#levelSpinner");
                    Group helperGroup = (Group) node.lookup("#helperGroup");

                    IssueType issueType = (IssueType) helperGroup.getProperties().get("issueType");

                    if (issueType != null) {
                        issueType.setLevel(levelSpinner.getValue());
                        issueType.setName(nameTextField.getText());
                        issueTypesToSave.add(issueType);
                    }else {
                        issueTypesToSave.add(new IssueType(null,nameTextField.getText(), levelSpinner.getValue(), null, false));
                    }

                }
            }

            try {
                Database.saveIssueTypes(issueTypesToSave);
                new MsgBox("Salvataggio effettuato", "Salvataggio effettuato con successo", Alert.AlertType.INFORMATION).launch();
            } catch (SQLException e) {
                new MsgBox("Salvataggio fallito", "Salvataggio fallito: " + e.getMessage(), Alert.AlertType.ERROR).launch();
            } finally {
                saved = true;
            }

            Stage currentStage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();


            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApp.class.getResource("EditLevelScreen.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load());
                currentStage.setScene(scene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });





    }
}
