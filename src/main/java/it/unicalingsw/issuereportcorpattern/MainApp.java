package it.unicalingsw.issuereportcorpattern;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    public static FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(MainApp.class.getResource("Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 700);
        stage.setTitle("CoR Pattern - IssueReportingSystem");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}