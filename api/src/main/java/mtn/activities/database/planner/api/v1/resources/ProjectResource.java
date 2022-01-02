package mtn.activities.database.planner.api.v1.resources;

import mtn.activities.database.planner.lib.Project;
import mtn.activities.database.planner.services.beans.ProjectBean;

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
@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProjectResource {
    private final Logger log = Logger.getLogger(ProjectResource.class.getName());

    @Inject
    private ProjectBean projectBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getProject() {
        List<Project> projects = projectBean.getProjectsFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(projects).build();
    }

    @POST
    public Response createProject(Project project) {
        if (project == null || project.getName() == null ||
                !projectBean.validateCategory(project.getCategoryId()))
            return Response.status(Response.Status.BAD_REQUEST).build();

        // TODO: validate position and fix if necessary
        if(project.getPosition() == null)
            project.setPosition(0);
        if(project.isFilledOut() == null)
            project.setFilledOut(false);

        project = projectBean.createProject(project);

        if(project == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.OK).entity(project).build();
    }

    @GET
    @Path("/{projectId}")
    public Response getProject(@PathParam("projectId") Integer projectId) {
        Project project = projectBean.getProject(projectId);

        if(project == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.OK).entity(project).build();
    }

    @PUT
    @Path("/{projectId}")
    public Response putProject(@PathParam("projectId") Integer projectId, Project project) {
        if (project == null || project.getName() == null ||
                !projectBean.validateCategory(project.getCategoryId()))
            return Response.status(Response.Status.BAD_REQUEST).build();
        if(projectBean.getProject(projectId) == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        // TODO: validate position and fix if necessary
        if(project.getPosition() == null)
            project.setPosition(0);
        if(project.isFilledOut() == null)
            project.setFilledOut(false);

        project = projectBean.putProject(projectId, project);
        if (project == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.OK).entity(project).build();
    }

    @DELETE
    @Path("/{projectId}")
    public Response deleteProject(@PathParam("projectId") Integer projectId) {
        if(projectBean.getProject(projectId) == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        boolean deleted = projectBean.deleteProject(projectId);
        if (deleted)
            return Response.status(Response.Status.NO_CONTENT).build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
