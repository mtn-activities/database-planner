package mtn.activities.database.planner.models.entities;

import javax.persistence.*;

@Entity
@Table(name="project")
@NamedQueries(value =
        {
                @NamedQuery(name = "ProjectEntity.getAll",
                        query = "SELECT p FROM ProjectEntity p")
        })
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="position")
    private Integer position;

    @Column(name="filledOut")
    private Boolean filledOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private CategoryEntity category;

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

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
