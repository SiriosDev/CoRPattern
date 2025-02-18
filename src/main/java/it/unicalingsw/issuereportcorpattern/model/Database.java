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
            issueTypes.add(new IssueType(rs.getInt("ID"),rs.getString("name"), rs.getInt("level"), (Integer) rs.getObject("defaultLevel"), rs.getBoolean("default")));;
        }
        stmt.close();

        return issueTypes;

    }

    public static void saveIssueTypes(ObservableList<IssueType> issueTypesToSave) throws SQLException {

        PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM IssueType");
        deleteStmt.executeUpdate();
        deleteStmt.close();

        for (IssueType issueType : issueTypesToSave) {
            PreparedStatement insertStmt = null;
            if (issueType.getID() == null) {
                insertStmt = connection.prepareStatement("INSERT INTO IssueType (Name, Level, DefaultLevel, \"Default\") VALUES (?, ?, ?, ?)");
            }else {
                insertStmt = connection.prepareStatement("INSERT INTO IssueType (Name, Level, DefaultLevel, \"Default\", ID) VALUES (?, ?, ?, ?, ?)");
                insertStmt.setInt(5, issueType.getID());
            }

            insertStmt.setString(1, issueType.getName());
            insertStmt.setInt(2, issueType.getLevel());
            if (issueType.getDefaultLevel() != null) {
                insertStmt.setInt(3, issueType.getDefaultLevel());
            } else {
                insertStmt.setNull(3, java.sql.Types.INTEGER);
            }
            insertStmt.setBoolean(4, issueType.getDefaultState());
            insertStmt.executeUpdate();
            insertStmt.close();
        }

        PreparedStatement deleteStmt2 = connection.prepareStatement("DELETE FROM Issue WHERE Type NOT IN (SELECT ID FROM IssueType)");
        deleteStmt2.executeUpdate();
        deleteStmt2.close();

    }

    private static IssueType findIssueByName(ObservableList<IssueType> list, String findName){
        return list.stream()
                .filter(e -> e != null && e.getName().equals(findName))
                .findFirst()
                .orElse(null);
    }

    public static ObservableList<Issue> getAllIssues(ObservableList<IssueType> issueTypes) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Issue as I , IssueType as IT WHERE I.Type==IT.ID ORDER BY IT.Level, I.Urgent DESC, I.Title");
        ObservableList<Issue> issues = FXCollections.observableArrayList();
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            issues.add(new Issue(findIssueByName(issueTypes, rs.getString("Name")), rs.getString("Title"), rs.getString("Desc"), rs.getBoolean("Urgent")));

        }
        return issues;
    }

    public static void addIssue(IssueType handlerIssueType, Issue issue) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Issue (Type, Title, Desc, Urgent) VALUES (?, ?, ?, ?)");
        stmt.setInt(1, handlerIssueType.getID());
        stmt.setString(2, issue.getTitle());
        stmt.setString(3, issue.getDescription());
        stmt.setBoolean(4, issue.getUrgent());
        stmt.executeUpdate();
        stmt.close();
    }
}
