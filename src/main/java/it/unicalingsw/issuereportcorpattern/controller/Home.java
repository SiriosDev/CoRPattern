package it.unicalingsw.issuereportcorpattern.controller;

import it.unicalingsw.issuereportcorpattern.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class Home implements Initializable {
    private static Home instance;
    @FXML
    private AnchorPane showPane;
    @FXML
    private Button creaButton;
    @FXML
    private Button visionaButton;

    public static Home getInstance() {
        if (Home.instance == null) {
            synchronized (Home.class) {
                if (Home.instance == null) {
                    Home.instance = new Home();
                }
            }
        }
        return Home.instance;
    }


    public void loadContent(String page) throws IOException {
        FXMLLoader loaderHelper = new FXMLLoader(Objects.requireNonNull(MainApp.class.getResource(page)));
        showPane.getChildren().clear();
        showPane.getChildren().add(loaderHelper.load());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadContent("ReportScreen.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        creaButton.setOnAction(event -> {
            try {
                loadContent("ReportScreen.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        visionaButton.setOnAction(event -> {
            try {
                loadContent("IssuesScreen.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
