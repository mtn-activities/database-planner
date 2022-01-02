package mtn.activities.database.planner.models.converters;

import mtn.activities.database.planner.lib.Project;
import mtn.activities.database.planner.models.entities.CategoryEntity;
import mtn.activities.database.planner.models.entities.ProjectEntity;

public class ProjectConverter {
    public static Project toDto(ProjectEntity entity) {
        Project dto = new Project();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPosition(entity.getPosition());
        dto.setFilledOut(entity.isFilledOut());
        if(entity.getCategory() != null)
            dto.setCategoryId(entity.getCategory().getId());
        return dto;
    }

    public static ProjectEntity toEntity(Project dto, CategoryEntity categoryEntity) {
        ProjectEntity entity = new ProjectEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPosition(dto.getPosition());
        entity.setFilledOut(dto.isFilledOut());
        entity.setCategory(categoryEntity);
        return entity;
    }
}
