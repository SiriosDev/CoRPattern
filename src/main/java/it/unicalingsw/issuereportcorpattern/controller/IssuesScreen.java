package it.unicalingsw.issuereportcorpattern.controller;

import it.unicalingsw.issuereportcorpattern.model.Issue;
import it.unicalingsw.issuereportcorpattern.model.IssueType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class IssuesScreen implements Initializable {

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


        ObservableList<IssueType> possibleIssueType = FXCollections.observableArrayList(
                null,
                new IssueType("Base",0),
                new IssueType("Intermedio",1),
                new IssueType("Avanzato",2),
                new IssueType("Critico",3)
        );

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

        ObservableList<Issue> issues = FXCollections.observableArrayList(
                new Issue(new IssueType("Base",0),"Prova","Prova",false),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true),
                new Issue(new IssueType("Base",0),"Prova","Prova",true)
        );

        issueComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            ObservableList<Issue> issuesToShow = FXCollections.observableArrayList();
            for (Issue issue : issues) {
                if (newValue == null || issue.getType().getLevel().equals(newValue.getLevel())) {
                    issuesToShow.add(issue);
                }
            }
            issueListView.setItems(issuesToShow);
        });

        issueListView.setCellFactory(new Callback<ListView<Issue>, ListCell<Issue>>() {
            @Override
            public ListCell<Issue> call(ListView<Issue> issueListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Issue issue, boolean empty) {
                        super.updateItem(issue, empty);
                        if (issue != null) {
                            setText(issue.getTitle());
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

                Text title = new Text(newValue.getTitle() +( newValue.getUrgent()?" - Urgente!":"")+"\n\n");
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



    }
}
