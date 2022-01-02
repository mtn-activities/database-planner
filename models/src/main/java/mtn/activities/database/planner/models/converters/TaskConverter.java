package mtn.activities.database.planner.models.converters;

import mtn.activities.database.planner.lib.Task;
import mtn.activities.database.planner.models.entities.CategoryEntity;
import mtn.activities.database.planner.models.entities.ProjectEntity;
import mtn.activities.database.planner.models.entities.TaskEntity;

public class TaskConverter {
    public static Task toDto(TaskEntity entity) {
        Task dto = new Task();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPosition(entity.getPosition());
        dto.setPriority(entity.getPriority());
        dto.setChecked(entity.getChecked());
        if(entity.getCategory() != null)
            dto.setCategoryId(entity.getCategory().getId());
        if(entity.getProject() != null)
            dto.setProjectId(entity.getProject().getId());
        return dto;
    }

    public static TaskEntity toEntity(Task dto, ProjectEntity projectEntity, CategoryEntity categoryEntity) {
        TaskEntity entity = new TaskEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPosition(dto.getPosition());
        entity.setPriority(dto.getPriority());
        entity.setChecked(dto.getChecked());
        entity.setProject(projectEntity);
        entity.setCategory(categoryEntity);
        return entity;
    }
}
