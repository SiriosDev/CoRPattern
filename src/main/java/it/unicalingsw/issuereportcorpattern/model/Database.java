package it.unicalingsw.issuereportcorpattern.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private static final Connection connection = ConnectionManager.getInstance().getConnection("db/database.db");

    public static ObservableList<IssueType> getAllIssueTypes() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM IssueType");
        ObservableList<IssueType> issueTypes = FXCollections.observableArrayList();
        issueTypes.add(null);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            issueTypes.add(new IssueType(rs.getString("name"), rs.getInt("level"), rs.getInt("defaultLevel"), rs.getBoolean("default")));;
        }
        stmt.close();
        return issueTypes;

    }
}
