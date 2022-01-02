package mtn.activities.database.planner.models.entities;

import javax.persistence.*;

@Entity
@Table(name="task")
@NamedQueries(value =
        {
                @NamedQuery(name = "TaskEntity.getAll",
                        query = "SELECT t FROM TaskEntity t")
        })
public class TaskEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name="name")
        private String name;

        @Column(name="checked")
        private Boolean checked;

        @Column(name="priority")
        private Integer priority;

        @Column(name="position")
        private Integer position;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category")
        private CategoryEntity category;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "project")
        private ProjectEntity project;

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

        public Boolean getChecked() {
                return checked;
        }

        public void setChecked(Boolean checked) {
                this.checked = checked;
        }

        public Integer getPriority() {
                return priority;
        }

        public void setPriority(Integer priority) {
                this.priority = priority;
        }

        public Integer getPosition() {
                return position;
        }

        public void setPosition(Integer position) {
                this.position = position;
        }

        public CategoryEntity getCategory() {
                return category;
        }

        public void setCategory(CategoryEntity category) {
                this.category = category;
        }

        public ProjectEntity getProject() {
                return project;
        }

        public void setProject(ProjectEntity project) {
                this.project = project;
        }
}
