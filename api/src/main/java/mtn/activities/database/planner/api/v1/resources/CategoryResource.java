package mtn.activities.database.planner.api.v1.resources;

import mtn.activities.database.planner.lib.Category;
import mtn.activities.database.planner.services.beans.CategoryBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {
    private final Logger log = Logger.getLogger(CategoryResource.class.getName());

    @Inject
    private CategoryBean categoryBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getCategory() {
        List<Category> categories = categoryBean.getCategoriesFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(categories).build();
    }

    @POST
    public Response createCategory(Category category) {
        if (category == null || category.getName() == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        // TODO: validate position and fix if necessary
        if(category.getPosition() == null)
            category.setPosition(0);
        if(category.getColor() == null)
            category.setColor(0);

        category = categoryBean.createCategory(category);

        if(category == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.OK).entity(category).build();
    }

    @GET
    @Path("/{categoryId}")
    public Response getCategory(@PathParam("categoryId") Integer categoryId) {
        Category category = categoryBean.getCategory(categoryId);

        if(category == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.OK).entity(category).build();
    }

    @PUT
    @Path("/{categoryId}")
    public Response putCategory(@PathParam("categoryId") Integer categoryId, Category category) {
        if (category == null || category.getName() == null)
            return Response.status(Response.Status.BAD_REQUEST).build();
        if(categoryBean.getCategory(categoryId) == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        // TODO: validate position and fix if necessary
        if(category.getPosition() == null)
            category.setPosition(0);
        if(category.getColor() == null)
            category.setColor(0);

        category = categoryBean.putCategory(categoryId, category);
        if (category == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.OK).entity(category).build();
    }

    @DELETE
    @Path("/{categoryId}")
    public Response deleteCategory(@PathParam("categoryId") Integer categoryId) {
        if(categoryBean.getCategory(categoryId) == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        boolean deleted = categoryBean.deleteCategory(categoryId);
        if (deleted)
            return Response.status(Response.Status.NO_CONTENT).build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
