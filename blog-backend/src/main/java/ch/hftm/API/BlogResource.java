package ch.hftm.API;

import java.util.List;
import java.util.Set;

import ch.hftm.Entities.Blog;
import ch.hftm.Entities.Post;
import ch.hftm.Entities.Link;
import ch.hftm.Repositories.BlogRepository;
import ch.hftm.Repositories.PostRepository;
import io.vertx.ext.web.handler.HttpException;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

/**
 * Main entry point for the blog
 * Will hand off requests to corresponding Subresource Locators
 */
@Path("blog")
public class BlogResource {

    @Inject
    Logger log;

    @Context
    UriInfo uriInfo;

    @Inject
    BlogRepository blogRepository;

    @Inject
    PostResource postResource;

    @Inject
    BlogUserResource userResource;

    @Inject
    Validator validator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Blog getBlog() {
        log.info("Getting blog");
        Blog blog = blogRepository.getBlog();

        log.info("Adding links to blog");
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

        log.info("Finished building blog response");

        return blog;
    }

    @PUT
    public void updateBlog(Blog blog) {
        Set<ConstraintViolation<Blog>> violations = validator.validate(blog);
        if (!violations.isEmpty()) {
            throw new HttpException(400, violations.stream().toString());
        }
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
