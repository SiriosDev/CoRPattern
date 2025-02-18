package it.unicalingsw.issuereportcorpattern.model.CoR;

import it.unicalingsw.issuereportcorpattern.model.Issue;

import java.sql.SQLException;

public abstract class IssueHandler {
    protected IssueHandler next;

    public void setNext(IssueHandler next) {
        this.next = next;
    }

    public abstract void handle(Issue issue) throws SQLException;
}