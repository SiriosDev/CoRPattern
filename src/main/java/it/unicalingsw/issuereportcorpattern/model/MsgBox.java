package it.unicalingsw.issuereportcorpattern.model;

import javafx.scene.control.Alert;

public class MsgBox {

    private Alert alert;

    public Alert getAlert() {
        return alert;
    }

    public MsgBox(String title, String message, Alert.AlertType alertType) {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);

    }

    public MsgBox(String title, String message, String content, Alert.AlertType alertType) {
        alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(content);

    }

    public void launch() {
        alert.showAndWait();
    }

}
