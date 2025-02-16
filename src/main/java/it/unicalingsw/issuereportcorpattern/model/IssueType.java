package it.unicalingsw.issuereportcorpattern.model;

public class IssueType {

    private String name;
    private Integer level;
    private Integer defaultLevel;

    public IssueType(String name, Integer level, Integer defaultLevel) {
        this.name = name;
        this.level = level;
        this.defaultLevel = defaultLevel;
    }

    public IssueType(String name, Integer level) {
        this.name = name;
        this.level = level;
        this.defaultLevel = level;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(Integer defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    public void resetLevel() {
        this.level = this.defaultLevel;
    }
}


