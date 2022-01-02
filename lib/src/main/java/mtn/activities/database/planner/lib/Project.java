package mtn.activities.database.planner.lib;

public class Project {
    private Integer id;
    private String name;
    private Integer position;
    private Boolean filledOut;
    private Integer categoryId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean isFilledOut() {
        return filledOut;
    }

    public void setFilledOut(Boolean filledOut) {
        this.filledOut = filledOut;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
