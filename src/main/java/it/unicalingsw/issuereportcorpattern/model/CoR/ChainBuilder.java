package it.unicalingsw.issuereportcorpattern.model.CoR;

import it.unicalingsw.issuereportcorpattern.model.IssueType;
import javafx.collections.ObservableList;

import java.util.List;


public class ChainBuilder {
    public static IssueHandler buildChain(ObservableList<IssueType> handlerIssueType) {
        IssueHandler first = null, current = null;

        for (IssueType issueType : handlerIssueType) {
            IssueHandler newIssueHandler = new DynamicIssueHandler(issueType);
            if (first == null) {
                first = newIssueHandler;
                current = first;
            } else {
                current.setNext(newIssueHandler);
                current = newIssueHandler;
            }
        }
        return first;
    }
}
