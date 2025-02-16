module it.unicalingsw.issuereportcorpattern {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;


    opens it.unicalingsw.issuereportcorpattern to javafx.fxml;
    exports it.unicalingsw.issuereportcorpattern;
    opens it.unicalingsw.issuereportcorpattern.controller to javafx.fxml;
    exports it.unicalingsw.issuereportcorpattern.controller to javafx.fxml;
}