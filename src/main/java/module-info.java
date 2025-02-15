module it.unicalingsw.issuereportcorpattern {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.unicalingsw.issuereportcorpattern to javafx.fxml;
    exports it.unicalingsw.issuereportcorpattern;
    exports it.unicalingsw.issuereportcorpattern.controller;
    opens it.unicalingsw.issuereportcorpattern.controller to javafx.fxml;
}