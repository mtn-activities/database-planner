package mtn.activities.database.planner.models.converters;

import mtn.activities.database.planner.lib.Category;
import mtn.activities.database.planner.models.entities.CategoryEntity;

public class CategoryConverter {
    public static Category toDto(CategoryEntity entity) {
        Category dto = new Category();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPosition(entity.getPosition());
        dto.setColor(entity.getColor());
        return dto;
    }

    public static CategoryEntity toEntity(Category dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPosition(dto.getPosition());
        entity.setColor(dto.getColor());
        return entity;
    }
}
