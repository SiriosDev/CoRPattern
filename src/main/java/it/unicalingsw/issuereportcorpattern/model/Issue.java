package it.unicalingsw.issuereportcorpattern.model;

public class Issue {
    private IssueType type;
    private String title;
    private String description;
    private Boolean isUrgent;

    public Issue(IssueType type, String title, String description, Boolean isUrgent) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.isUrgent = isUrgent;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }
}
