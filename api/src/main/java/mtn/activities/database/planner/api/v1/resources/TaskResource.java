package mtn.activities.database.planner.api.v1.resources;

import mtn.activities.database.planner.lib.Task;
import mtn.activities.database.planner.services.beans.TaskBean;

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
@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {
    private final Logger log = Logger.getLogger(TaskResource.class.getName());

    @Inject
    private TaskBean taskBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getTask() {
        List<Task> tasks = taskBean.getTasksFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(tasks).build();
    }

    @POST
    public Response createTask(Task task) {
        if (task == null || task.getName() == null ||
                !taskBean.validateCategoryProject(task.getCategoryId(), task.getProjectId()))
            return Response.status(Response.Status.BAD_REQUEST).build();

        // TODO: validate position and fix if necessary
        if(task.getPosition() == null)
            task.setPosition(0);
        if(task.getPriority() == null)
            task.setPriority(0);
        if(task.getChecked() == null)
            task.setChecked(true);

        task = taskBean.createTask(task);

        if(task == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.OK).entity(task).build();
    }

    @GET
    @Path("/{taskId}")
    public Response getTask(@PathParam("taskId") Integer taskId) {
        Task task = taskBean.getTask(taskId);

        if(task == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.OK).entity(task).build();
    }

    @PUT
    @Path("/{taskId}")
    public Response putTask(@PathParam("taskId") Integer taskId, Task task) {
        if (task == null || task.getName() == null ||
                !taskBean.validateCategoryProject(task.getCategoryId(), task.getProjectId()))
            return Response.status(Response.Status.BAD_REQUEST).build();
        if(taskBean.getTask(taskId) == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        // TODO: validate position and fix if necessary
        if(task.getPosition() == null)
            task.setPosition(0);
        if(task.getPriority() == null)
            task.setPriority(0);
        if(task.getChecked() == null)
            task.setChecked(true);

        task = taskBean.putTask(taskId, task);
        if (task == null)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        return Response.status(Response.Status.OK).entity(task).build();
    }

    @DELETE
    @Path("/{taskId}")
    public Response deleteTask(@PathParam("taskId") Integer taskId) {
        if(taskBean.getTask(taskId) == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        boolean deleted = taskBean.deleteTask(taskId);
        if (deleted)
            return Response.status(Response.Status.NO_CONTENT).build();
        else
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
