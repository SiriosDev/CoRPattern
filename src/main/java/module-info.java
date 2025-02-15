module it.unicalingsw.issuereportcorpattern {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.unicalingsw.issuereportcorpattern to javafx.fxml;
    exports it.unicalingsw.issuereportcorpattern;
}