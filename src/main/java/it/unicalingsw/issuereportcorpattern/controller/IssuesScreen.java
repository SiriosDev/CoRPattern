package it.unicalingsw.issuereportcorpattern.controller;

import it.unicalingsw.issuereportcorpattern.MainApp;
import it.unicalingsw.issuereportcorpattern.model.Database;
import it.unicalingsw.issuereportcorpattern.model.Issue;
import it.unicalingsw.issuereportcorpattern.model.IssueType;
import it.unicalingsw.issuereportcorpattern.model.MsgBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class IssuesScreen implements Initializable {

    @FXML
    private Button editButton;
    @FXML
    private AnchorPane detailSide;
    @FXML
    private SplitPane splitPane;
    @FXML
    private ListView<Issue> issueListView;
    @FXML
    private ComboBox<IssueType> issueComboBox;
    @FXML
    private TextFlow issueTitleFlow;
    @FXML
    private TextFlow issueDescFlow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        double minLimit = 0.4;
        double maxLimit = 0.7;
        double snapThreshold = 0.05;

        splitPane.setDividerPositions(0.5);

        splitPane.getDividers().get(0).positionProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double position = newValue.doubleValue();

                if (position < minLimit) {
                    if (position < snapThreshold) {
                        // Snap completo a sinistra
                        splitPane.setDividerPositions(0.0);
                    } else {
                        // Blocca al limite minimo
                        splitPane.setDividerPositions(minLimit);
                    }
                } else if (position > maxLimit) {
                    if (position > 1.0 - snapThreshold) {
                        // Snap completo a destra
                        splitPane.setDividerPositions(1.0);
                    } else {
                        // Blocca al limite massimo
                        splitPane.setDividerPositions(maxLimit);
                    }
                }
            }
        });


        ObservableList<IssueType> possibleIssueType = null;
        try {
            possibleIssueType = Database.getAllIssueTypes();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        issueComboBox.setItems(possibleIssueType);

        issueComboBox.setConverter(new StringConverter<IssueType>() {
            @Override
            public String toString(IssueType issueType) {
                return issueType != null ? issueType.getName() : null;
            }

            @Override
            public IssueType fromString(String s) {
                return null;
            }
        });

        issueComboBox.setCellFactory(new Callback<ListView<IssueType>, ListCell<IssueType>>() {
            @Override
            public ListCell<IssueType> call(ListView<IssueType> issueTypeListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(IssueType issueType, boolean empty) {
                        super.updateItem(issueType, empty);
                        if (issueType != null) {
                            setText(issueType.getName());
                        }else{
                            setText(null);
                        }
                    }
                };
            }
        });


        try {
            ObservableList<Issue> issues = Database.getAllIssues(possibleIssueType);

            issueComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
                ObservableList<Issue> issuesToShow = FXCollections.observableArrayList();
                for (Issue issue : issues) {
                    if (newValue != null && issue.getType().getLevel().equals(newValue.getLevel())) {
                        issuesToShow.add(issue);
                    }
                }
                issueListView.setItems(issuesToShow);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        issueListView.setCellFactory(new Callback<ListView<Issue>, ListCell<Issue>>() {
            @Override
            public ListCell<Issue> call(ListView<Issue> issueListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Issue issue, boolean empty) {
                        super.updateItem(issue, empty);
                        if (issue != null) {
                            setText(issue.getTitle() + ( issue.getUrgent()?" - Urgente!":""));
                        }else{
                            setText(null);
                        }
                    }
                };
            }
        });

        String fontFamily = "Helvetica";

        issueTitleFlow.setStyle("-fx-text-alignment: center;");
        issueTitleFlow.getChildren().clear();
        Text noIssue = new Text("Nessun report selezionato");
        noIssue.setFont(Font.font(fontFamily, FontWeight.BOLD, 30));
        issueTitleFlow.getChildren().add(noIssue);

        issueListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                Text title = new Text(newValue.getTitle() + ( newValue.getUrgent()?" - Urgente!":"")+"\n\n");
                title.setFont(Font.font(fontFamily, FontWeight.BOLD, 30));
                Text desc = new Text(newValue.getDescription());
                desc.setFont(Font.font(fontFamily, 20));

                issueTitleFlow.getChildren().clear();
                issueTitleFlow.getChildren().add(title);
                issueDescFlow.getChildren().clear();
                issueDescFlow.getChildren().add(desc);

            } else {
                issueTitleFlow.getChildren().clear();
                issueDescFlow.getChildren().clear();
                issueTitleFlow.getChildren().add(noIssue);
            }
        });

        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage dialogStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(MainApp.class.getResource("EditLevelScreen.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    dialogStage.setTitle("Modifica Livelli");
                    dialogStage.setResizable(false);
                    dialogStage.initModality(Modality.APPLICATION_MODAL);
                    dialogStage.setScene(scene);
                    dialogStage.show();

                    dialogStage.setOnCloseRequest(ev -> {

                        EditLevelScreen controller = fxmlLoader.getController();
                        if (!controller.saved) {
                            MsgBox warning = new MsgBox("Attenzione! Stai uscendo senza salvare", "Sei sicuro di voler chiudere la schermata senza salvare?", Alert.AlertType.WARNING);
                            warning.getAlert().initOwner(dialogStage);
                            warning.getAlert().getButtonTypes().clear();
                            ButtonType continua = new ButtonType("Continua");
                            ButtonType annulla = new ButtonType("Annulla");
                            warning.getAlert().getButtonTypes().addAll(continua, annulla);

                            Optional<ButtonType> result = warning.getAlert().showAndWait();
                            if (result.isPresent() && result.get() == annulla) {
                                ev.consume();
                            } else {
                                dialogStage.close();
                            }
                        } else {
                            dialogStage.close();
                        }


                        Home home = MainApp.fxmlLoader.getController();
                        try {
                            home.loadContent("IssuesScreen.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });



    }
}
