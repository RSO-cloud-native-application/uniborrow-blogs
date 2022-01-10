package si.fri.rso.uniborrow.blogs.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;
import com.kumuluz.ee.logs.cdi.Log;

import si.fri.rso.uniborrow.blogs.lib.Blog;
import si.fri.rso.uniborrow.blogs.models.entities.BlogEntity;
import si.fri.rso.uniborrow.blogs.services.beans.BlogBean;
import si.fri.rso.uniborrow.blogs.services.config.RestProperties;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.uniborrow.blogs.services.users.UsersService;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.metrics.annotation.Metered;

@Log
@ApplicationScoped
@Path("/blogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class BlogsResource {

    private final Logger log = Logger.getLogger(BlogsResource.class.getName());

    @Inject
    private BlogBean blogBean;

    @Inject
    private RestProperties rp;

    @Context
    protected UriInfo uriInfo;

    @Context
    private UsersService usersService;

    @GET
    @Timed(name = "get_blogs_time")
    @Operation(description = "Get blogs by filter, or all.", summary = "Get blogs by filter, or all.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Blogs that fit the filter.",
                    content = @Content(schema = @Schema(implementation = BlogEntity.class, type = SchemaType.ARRAY))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "No blogs found."
            )
    })
    public Response getBlogs() {
        List<BlogEntity> blogs = blogBean.getBlogsFilter(uriInfo);
        return Response.status(200).entity(blogs).build();
    }

    @GET
    @Path("/{blogId}")
    @Operation(description = "Get blog by id.", summary = "Get blog by id.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Blog by id.",
                    content = @Content(schema = @Schema(implementation = BlogEntity.class))
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Blog with id not found."
            )
    })
    public Response getBlog(@PathParam("blogId") Integer blogId) {
        Blog blog = blogBean.getBlog(blogId);
        if (blog == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (rp.getMaintenanceMode()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(blog).build();
    }

    @POST
    @Timed(name = "post_blogs_time")
    @Operation(description = "Create new blog.", summary = "Create new blog.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Created Blog",
                    content = @Content(schema = @Schema(implementation = BlogEntity.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Problems with blog body."
            )
    })
    public Response createBlog(BlogEntity blogEntity) {
        if (blogEntity.getTitle() == null ||
                blogEntity.getText() == null || blogEntity.getUserId() == null) {
            return Response.status(300).build();
        } else {
            if (!usersService.checkUserExists(blogEntity.getUserId())) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            blogEntity.setTimestamp(Instant.now());
            blogEntity = blogBean.createBlog(blogEntity);
        }
        return Response.status(Response.Status.OK).entity(blogEntity).build();
    }

    @PUT
    @Path("{blogId}")
    @Operation(description = "Edit a blog.", summary = "Edit a blog.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Edited blog",
                    content = @Content(schema = @Schema(implementation = BlogEntity.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Problems with blog body."
            )
    })
    public Response updateBlog(Blog blog, @PathParam("blogId") Integer blogId) {

        blog.setTimestamp(Instant.now());
        blog = blogBean.putBlog(blog, blogId);
        if (blog == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @PATCH
    @Path("{blogId}")
    @Operation(description = "Patch a blog.", summary = "Patch a blog.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Patched blog",
                    content = @Content(schema = @Schema(implementation = BlogEntity.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Problems with blog body."
            )
    })
    public Response patchBlog(Blog blog, @PathParam("blogId") Integer blogId) {
        blog = blogBean.patchBlog(blog, blogId);
        if (blog == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{blogId}")
    @Timed(name = "delete_blogs_time")
    @Operation(description = "Delete a blog.", summary = "Delete a blog.")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Blog deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Blog not found."
            )
    })
    public Response deleteBlog(@PathParam("blogId") Integer blogId) {
        boolean isSuccessful = blogBean.deleteBlog(blogId);
        if (isSuccessful) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}