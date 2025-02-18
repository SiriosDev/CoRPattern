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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM IssueType ORDER BY Level");
        ObservableList<IssueType> issueTypes = FXCollections.observableArrayList();
        issueTypes.add(null);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            issueTypes.add(new IssueType(rs.getString("name"), rs.getInt("level"), (Integer) rs.getObject("defaultLevel"), rs.getBoolean("default")));;
        }
        stmt.close();



        return issueTypes;

    }

    public static void saveIssueTypes(ObservableList<IssueType> issueTypesToSave) throws SQLException {

        PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM IssueType");
        deleteStmt.executeUpdate();
        deleteStmt.close();

        PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO IssueType (Name, Level, DefaultLevel, \"Default\") VALUES (?, ?, ?, ?)");
        for (IssueType issueType : issueTypesToSave) {
            insertStmt.setString(1, issueType.getName());
            insertStmt.setInt(2, issueType.getLevel());
            if (issueType.getDefaultLevel() != null) {
                insertStmt.setInt(3, issueType.getDefaultLevel());
            } else {
                insertStmt.setNull(3, java.sql.Types.INTEGER);
            }
            insertStmt.setBoolean(4, issueType.getDefaultState());
            insertStmt.executeUpdate();
        }
        insertStmt.close();
    }
}
