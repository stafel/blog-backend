package ch.hftm.API;

import java.util.List;

import ch.hftm.Entities.Blog;
import ch.hftm.Entities.Post;
import ch.hftm.Entities.Link;
import ch.hftm.Repositories.BlogRepository;
import ch.hftm.Repositories.PostRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

/**
 * Main entry point for the blog
 * Will hand off requests to corresponding Subresource Locators
 */
@Path("blog")
public class BlogResource {

    @Context
    UriInfo uriInfo;

    @Inject
    BlogRepository blogRepository;

    @Inject
    PostResource postResource;

    @Inject
    BlogUserResource userResource;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Blog getBlog() {
        Blog blog = blogRepository.getBlog();

        // possibility to update this blog
        blog.addLink(
            new Link(
                uriInfo,
                BlogResource.class,
                "blog",
                "PUT"
                )
        );

        // possibility to read some posts
        blog.addLink(
            new Link(
                uriInfo,
                PostResource.class,
                "posts",
                "GET"
                )
        );

        // possibility to read some users
        blog.addLink(
            new Link(
                uriInfo,
                BlogUserResource.class,
                "users",
                "GET"
                )
        );

        return blog;
    }

    @PUT
    public void updateBlog(Blog blog) {
        blogRepository.updateBlog(blog);
    }

    @Path("posts")
    public Object posts() {
        return postResource;
    }

    @Path("users")
    public Object users() {
        return userResource;
    }
}
