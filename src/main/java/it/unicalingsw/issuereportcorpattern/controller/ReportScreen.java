package it.unicalingsw.issuereportcorpattern.controller;

import it.unicalingsw.issuereportcorpattern.model.Database;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportScreen  implements Initializable {

    @FXML
    private SplitPane wrapSend;
    @FXML
    private SplitPane wrapReset;
    @FXML
    private TextField titleTextField;
    @FXML
    private Label titleLimitLabel;
    @FXML
    private Label descLimitLabel;
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

    private Integer MAX_TITLE_LENGTH = 100;
    private Integer MAX_DESC_LENGTH = 3096;

    private Boolean disableByType=true;
    private Boolean disableByTitle=true;
    private Boolean disableByDesc=true;

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

    private void updateTooltip() {
        String tooltipText;
        Boolean disable=disableByType||disableByTitle||disableByDesc;

        if (disable) {
            tooltipText = "I seguenti campi devono essere compilati: \n";
            tooltipText += disableByType ? "- Tipo \n" : "";
            tooltipText += disableByTitle ? "- Titolo \n" : "";
            tooltipText += disableByDesc  ? "- Descrizione \n" : "";

            wrapSend.setTooltip(makeTooltip(tooltipText));
            wrapReset.setTooltip(makeTooltip(tooltipText));
        } else {
            wrapSend.setTooltip(null);
            wrapReset.setTooltip(null);
        }


        sendButton.setDisable(disable);
        resetButton.setDisable(disable);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

        wrapSend.setStyle("-fx-box-border: transparent;");
        wrapReset.setStyle("-fx-box-border: transparent;");


        titleLimitLabel.setText(MAX_TITLE_LENGTH+"");
        descLimitLabel.setText(MAX_DESC_LENGTH+"");
        updateTooltip();


        issueComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            disableByType = newValue == null || newValue.getName().isEmpty();
            updateTooltip();
        });

        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int length = newValue != null ? newValue.length() : 0;
            titleLimitLabel.setText((MAX_TITLE_LENGTH - length) + "");
            if (length > MAX_TITLE_LENGTH) {
                titleLimitLabel.setStyle("-fx-text-fill: red;");
            } else {
                titleLimitLabel.setStyle("-fx-text-fill: black;");
            }

            //titleTextField.setText(newValue.length()>limit ? newValue.substring(0,limit) : newValue);


            disableByTitle = newValue == null || newValue.isEmpty() || length > MAX_TITLE_LENGTH;
            updateTooltip();
        });

        descTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            int length = newValue != null ? newValue.length() : 0;
            descLimitLabel.setText((MAX_DESC_LENGTH - length) + "");
            if (length > MAX_DESC_LENGTH) {
                descLimitLabel.setStyle("-fx-text-fill: red;");
            } else {
                descLimitLabel.setStyle("-fx-text-fill: black;");
            }

            //descTextArea.setText(newValue.length()>limit ? newValue.substring(0,limit) : newValue);


            disableByDesc = newValue == null || newValue.isEmpty() || length > MAX_DESC_LENGTH;
            updateTooltip();
        });





        resetButton.setOnAction(event -> resetFields());

        sendButton.setOnAction(event -> {
            System.out.println("Issue: " + issueComboBox.getValue().getName());
            System.out.println("Titolo: " + titleTextField.getText());
            System.out.println("Descrizione: " + descTextArea.getText());
            System.out.println("Urgente: " + urgentCheckBox.isSelected());
            resetFields();
        });




    }
}
