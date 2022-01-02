package mtn.activities.database.planner.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import mtn.activities.database.planner.lib.Task;
import mtn.activities.database.planner.models.converters.TaskConverter;
import mtn.activities.database.planner.models.entities.CategoryEntity;
import mtn.activities.database.planner.models.entities.ProjectEntity;
import mtn.activities.database.planner.models.entities.TaskEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class TaskBean {

    private final Logger log = Logger.getLogger(TaskBean.class.getName());

    @Inject
    private EntityManager em;

    // generic GET query for all items
    public List<Task> getTasks() {
        TypedQuery<TaskEntity> query = em.createNamedQuery("TaskEntity.getAll", TaskEntity.class);

        List<TaskEntity> resultList = query.getResultList();
        return resultList.stream().map(TaskConverter::toDto).collect(Collectors.toList());
    }

    // GET request with parameters
    public List<Task> getTasksFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, TaskEntity.class, queryParameters).stream()
                .map(TaskConverter::toDto).collect(Collectors.toList());
    }

    public boolean validateCategoryProject(Integer categoryId, Integer projectId) {
        if(categoryId == null && projectId == null)
            return true;
        else if(categoryId == null) {
            ProjectEntity projectEntity = em.find(ProjectEntity.class, projectId);
            return projectEntity != null;
        } else if(projectId == null) {
            CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
            return categoryEntity != null;
        } else {
            ProjectEntity projectEntity = em.find(ProjectEntity.class, projectId);
            CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
            return projectEntity != null && categoryEntity != null && projectEntity.getCategory() != null &&
                    Objects.equals(projectEntity.getCategory().getId(), categoryId);
        }
    }

    // POST
    public Task createTask(Task task) {
        ProjectEntity projectEntity;
        CategoryEntity categoryEntity;

        if(task.getProjectId() == null && task.getCategoryId() == null) {
            projectEntity = null;
            categoryEntity = null;
        } else if(task.getProjectId() == null) {
            projectEntity = null;
            categoryEntity = em.find(CategoryEntity.class, task.getCategoryId());
            if(categoryEntity == null)
                return null;
        } else if(task.getCategoryId() == null) {
            projectEntity = em.find(ProjectEntity.class, task.getProjectId());
            if(projectEntity == null)
                return null;
            categoryEntity = projectEntity.getCategory();
        } else {
            projectEntity = em.find(ProjectEntity.class, task.getProjectId());
            categoryEntity = em.find(CategoryEntity.class, task.getCategoryId());
            if(projectEntity == null || categoryEntity == null || projectEntity.getCategory() == null
                    || !Objects.equals(projectEntity.getCategory().getId(), categoryEntity.getId()))
                return null;
        }

        TaskEntity taskEntity = TaskConverter.toEntity(task, projectEntity, categoryEntity);

        try {
            beginTx();
            em.persist(taskEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (taskEntity.getId() == null)
            return null;

        return TaskConverter.toDto(taskEntity);
    }

    // GET by id
    public Task getTask(Integer taskId) {
        TaskEntity taskEntity = em.find(TaskEntity.class, taskId);

        if(taskEntity == null)
            return null;
        return TaskConverter.toDto(taskEntity);
    }

    // PUT by id
    public Task putTask(Integer taskId, Task task) {
        TaskEntity taskEntity = em.find(TaskEntity.class, taskId);
        if (taskEntity == null)
            return null;

        ProjectEntity projectEntity;
        CategoryEntity categoryEntity;

        if(task.getProjectId() == null && task.getCategoryId() == null) {
            projectEntity = null;
            categoryEntity = null;
        } else if(task.getProjectId() == null) {
            projectEntity = null;
            categoryEntity = em.find(CategoryEntity.class, task.getCategoryId());
            if(categoryEntity == null)
                return null;
        } else if(task.getCategoryId() == null) {
            projectEntity = em.find(ProjectEntity.class, task.getProjectId());
            if(projectEntity == null)
                return null;
            categoryEntity = projectEntity.getCategory();
        } else {
            projectEntity = em.find(ProjectEntity.class, task.getProjectId());
            categoryEntity = em.find(CategoryEntity.class, task.getCategoryId());
            if(projectEntity == null || categoryEntity == null || projectEntity.getCategory() == null
                    || !Objects.equals(projectEntity.getCategory().getId(), categoryEntity.getId()))
                return null;
        }

        TaskEntity updatedTaskEntity = TaskConverter.toEntity(task, projectEntity, categoryEntity);
        try {
            beginTx();
            updatedTaskEntity.setId(taskEntity.getId());
            updatedTaskEntity = em.merge(updatedTaskEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            return null;
        }
        return TaskConverter.toDto(updatedTaskEntity);
    }

    // DELETE by id
    public boolean deleteTask(Integer taskId) {
        TaskEntity task = em.find(TaskEntity.class, taskId);
        if(task == null)
            return false;

        try {
            beginTx();
            em.remove(task);
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
