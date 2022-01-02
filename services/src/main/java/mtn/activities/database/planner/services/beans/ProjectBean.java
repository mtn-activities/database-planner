package mtn.activities.database.planner.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import mtn.activities.database.planner.lib.Project;
import mtn.activities.database.planner.models.converters.ProjectConverter;
import mtn.activities.database.planner.models.entities.CategoryEntity;
import mtn.activities.database.planner.models.entities.ProjectEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class ProjectBean {

    private final Logger log = Logger.getLogger(ProjectBean.class.getName());

    @Inject
    private EntityManager em;

    // generic GET query for all items
    public List<Project> getProjects() {
        TypedQuery<ProjectEntity> query = em.createNamedQuery("ProjectEntity.getAll", ProjectEntity.class);

        List<ProjectEntity> resultList = query.getResultList();
        return resultList.stream().map(ProjectConverter::toDto).collect(Collectors.toList());
    }

    // GET request with parameters
    public List<Project> getProjectsFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, ProjectEntity.class, queryParameters).stream()
                .map(ProjectConverter::toDto).collect(Collectors.toList());
    }

    public boolean validateCategory(Integer categoryId) {
        if(categoryId == null)
            return true;
        else {
            CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
            return categoryEntity != null;
        }
    }

    // POST
    public Project createProject(Project project) {
        CategoryEntity categoryEntity;

        if(project.getCategoryId() == null)
            categoryEntity = null;
        else {
            categoryEntity = em.find(CategoryEntity.class, project.getCategoryId());
            if(categoryEntity == null)
                return null;
        }

        ProjectEntity projectEntity = ProjectConverter.toEntity(project, categoryEntity);

        try {
            beginTx();
            em.persist(projectEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (projectEntity.getId() == null)
            return null;

        return ProjectConverter.toDto(projectEntity);
    }

    // GET by id
    public Project getProject(Integer projectId) {
        ProjectEntity projectEntity = em.find(ProjectEntity.class, projectId);

        if(projectEntity == null)
            return null;
        return ProjectConverter.toDto(projectEntity);
    }

    // PUT by id
    public Project putProject(Integer projectId, Project project) {
        ProjectEntity projectEntity = em.find(ProjectEntity.class, projectId);
        if (projectEntity == null)
            return null;

        CategoryEntity categoryEntity;
        if(project.getCategoryId() == null)
            categoryEntity = null;
        else {
            categoryEntity = em.find(CategoryEntity.class, project.getCategoryId());
            if(categoryEntity == null)
                return null;
        }

        ProjectEntity updatedProjectEntity = ProjectConverter.toEntity(project, categoryEntity);
        try {
            beginTx();
            updatedProjectEntity.setId(projectEntity.getId());
            updatedProjectEntity = em.merge(updatedProjectEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            return null;
        }
        return ProjectConverter.toDto(updatedProjectEntity);
    }

    // DELETE by id
    public boolean deleteProject(Integer projectId) {
        ProjectEntity project = em.find(ProjectEntity.class, projectId);
        if(project == null)
            return false;

        try {
            beginTx();
            em.remove(project);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
