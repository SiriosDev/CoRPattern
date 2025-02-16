package it.unicalingsw.issuereportcorpattern.controller;

import it.unicalingsw.issuereportcorpattern.model.IssueType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class ReportScreen  implements Initializable {

    @FXML
    private SplitPane wrapSend;
    @FXML
    private SplitPane wrapReset;
    @FXML
    private TextArea descTextArea;
    @FXML
    private CheckBox urgentCheckBox;
    @FXML
    private Button sendButton;
    @FXML
    private Button resetButton;
    @FXML
    private ComboBox<IssueType> issueComboBox;

    private void resetFields() {
        descTextArea.clear();
        urgentCheckBox.setSelected(false);
        issueComboBox.setValue(null);
    }

    private Tooltip makeTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.millis(5.0));
        tooltip.setHideDelay(Duration.millis(5.0));
        return tooltip;
    }

    private void updateTooltip(boolean disable) {
        String tooltipText;
        if (disable) {
            tooltipText = "I seguenti campi devono essere compilati: \n";
            tooltipText += (descTextArea.getText().isEmpty() || descTextArea.getText()==null)  ? "- Descrizione \n" : "";
            tooltipText += (issueComboBox.getValue() == null) ? "- Tipo \n" : "";
        } else {
            tooltipText="";
        }
        wrapSend.setTooltip(makeTooltip(tooltipText));
        wrapReset.setTooltip(makeTooltip(tooltipText));
    }

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


        sendButton.setDisable(true);
        resetButton.setDisable(true);
        updateTooltip(true);

        descTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean disable = newValue.isEmpty() || issueComboBox.getValue() == null;
            sendButton.setDisable(disable);
            resetButton.setDisable(disable);
            updateTooltip(disable);
        });

        issueComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            boolean disable = descTextArea.getText().isEmpty() || newValue == null;
            sendButton.setDisable(disable);
            resetButton.setDisable(disable);
            updateTooltip(disable);
        });



        resetButton.setOnAction(event -> resetFields());

        sendButton.setOnAction(event -> {
            System.out.println("Issue: " + issueComboBox.getValue().getName());
            System.out.println("Descrizione: " + descTextArea.getText());
            System.out.println("Urgente: " + urgentCheckBox.isSelected());
            resetFields();
        });




    }
}
