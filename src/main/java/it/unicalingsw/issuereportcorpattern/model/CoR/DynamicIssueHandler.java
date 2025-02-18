package it.unicalingsw.issuereportcorpattern.model.CoR;

import it.unicalingsw.issuereportcorpattern.model.Database;
import it.unicalingsw.issuereportcorpattern.model.Issue;
import it.unicalingsw.issuereportcorpattern.model.IssueType;

import java.sql.SQLException;

public class DynamicIssueHandler extends IssueHandler {
    private IssueType handlerIssueType;

    public DynamicIssueHandler(IssueType handlerIssueType) {
        this.handlerIssueType = handlerIssueType;
    }

    @Override
    public void handle(Issue issue) throws SQLException {
        if (issue.getType().equals(handlerIssueType)) {
            Database.addIssue(handlerIssueType,issue);
        } else {
            if (next != null) {
                next.handle(issue);
            }else {
                throw new RuntimeException("Nessun handler disponibile");
            }

        }
    }
}
