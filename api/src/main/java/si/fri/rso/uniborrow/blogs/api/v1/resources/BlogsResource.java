package si.fri.rso.uniborrow.blogs.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import com.kumuluz.ee.logs.cdi.Log;

import si.fri.rso.uniborrow.blogs.lib.Blog;
import si.fri.rso.uniborrow.blogs.models.entities.BlogEntity;
import si.fri.rso.uniborrow.blogs.services.beans.BlogBean;
import si.fri.rso.uniborrow.blogs.services.config.RestProperties;

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

    @GET
    public Response getBlogs() {
        List<BlogEntity> blogs = blogBean.getBlogsFilter(uriInfo);
        return Response.status(200).entity(blogs).build();
    }

    @GET
    @Path("/{blogId}")
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
    public Response createBlog(BlogEntity blogEntity) {
        if (blogEntity.getTitle() == null ||
                blogEntity.getText() == null || blogEntity.getUserId() == null
                || blogEntity.getTimestamp() == null) {
            return Response.status(300).build();
        } else {
            blogEntity = blogBean.createBlog(blogEntity);
        }
        return Response.status(Response.Status.OK).entity(blogEntity).build();
    }

    @PUT
    @Path("{blogId}")
    public Response updateBlog(Blog blog, @PathParam("blogId") Integer blogId) {

        blog = blogBean.putBlog(blog, blogId);
        if (blog == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @PATCH
    @Path("{blogId}")
    public Response patchBlog(Blog blog, @PathParam("blogId") Integer blogId) {
        blog = blogBean.patchBlog(blog, blogId);
        if (blog == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{blogId}")
    public Response deleteBlog(@PathParam("blogId") Integer blogId) {
        boolean isSuccessful = blogBean.deleteBlog(blogId);
        if (isSuccessful) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}