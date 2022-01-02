package mtn.activities.database.planner.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import mtn.activities.database.planner.lib.Category;
import mtn.activities.database.planner.models.converters.CategoryConverter;
import mtn.activities.database.planner.models.entities.CategoryEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class CategoryBean {

    private final Logger log = Logger.getLogger(CategoryBean.class.getName());

    @Inject
    private EntityManager em;

    // generic GET query for all items
    public List<Category> getCategories() {
        TypedQuery<CategoryEntity> query = em.createNamedQuery("CategoryEntity.getAll", CategoryEntity.class);

        List<CategoryEntity> resultList = query.getResultList();
        return resultList.stream().map(CategoryConverter::toDto).collect(Collectors.toList());
    }

    // GET request with parameters
    public List<Category> getCategoriesFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, CategoryEntity.class, queryParameters).stream()
                .map(CategoryConverter::toDto).collect(Collectors.toList());
    }

    // POST
    public Category createCategory(Category category) {
        CategoryEntity categoryEntity = CategoryConverter.toEntity(category);

        try {
            beginTx();
            em.persist(categoryEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        if (categoryEntity.getId() == null)
            return null;

        return CategoryConverter.toDto(categoryEntity);
    }

    // GET by id
    public Category getCategory(Integer categoryId) {
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);

        if(categoryEntity == null)
            return null;
        return CategoryConverter.toDto(categoryEntity);
    }

    // PUT by id
    public Category putCategory(Integer categoryId, Category category) {
        CategoryEntity categoryEntity = em.find(CategoryEntity.class, categoryId);
        if (categoryEntity == null)
            return null;

        CategoryEntity updatedCategoryEntity = CategoryConverter.toEntity(category);
        try {
            beginTx();
            updatedCategoryEntity.setId(categoryEntity.getId());
            updatedCategoryEntity = em.merge(updatedCategoryEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            return null;
        }
        return CategoryConverter.toDto(updatedCategoryEntity);
    }

    // DELETE by id
    public boolean deleteCategory(Integer categoryId) {
        CategoryEntity category = em.find(CategoryEntity.class, categoryId);
        if(category == null)
            return false;

        try {
            beginTx();
            em.remove(category);
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
