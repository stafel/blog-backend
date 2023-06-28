package ch.hftm.API;

import java.util.List;

import ch.hftm.Entities.Blog;
import ch.hftm.Entities.Post;
import ch.hftm.Entities.Link;
import ch.hftm.Repositories.BlogRepository;
import ch.hftm.Repositories.PostRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Blog getBlog() {
        Blog blog = blogRepository.getBlog();

        blog.addLink(
            new Link(
                uriInfo,
                PostResource.class,
                "posts",
                "GET"
                )
        );

        return blog;
    }

    @Path("posts")
    public Object posts() {
        return postResource;
    }
}
