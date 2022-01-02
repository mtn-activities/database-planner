package mtn.activities.database.planner.models.entities;

import javax.persistence.*;

@Entity
@Table(name="category")
@NamedQueries(value =
        {
                @NamedQuery(name = "CategoryEntity.getAll",
                        query = "SELECT c FROM CategoryEntity c")
        })
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="position")
    private Integer position;

    @Column(name="color")
    private Integer color;

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

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
