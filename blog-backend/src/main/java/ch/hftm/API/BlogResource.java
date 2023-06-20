package ch.hftm.API;

import java.util.List;

import ch.hftm.Entities.Post;
import ch.hftm.Repositories.PostRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Main entry point for the blog
 * Will hand off requests to corresponding Subresource Locators
 */
@Path("blog")
public class BlogResource {

    @Inject
    PostResource postResource;

    @Path("posts")
    public Object posts() {
        return postResource;
    }
}
