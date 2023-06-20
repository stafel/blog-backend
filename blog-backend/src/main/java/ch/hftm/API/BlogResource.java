package ch.hftm.API;

import java.util.List;

import ch.hftm.Entities.Blog;
import ch.hftm.Repositories.BlogRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("blogs")
public class BlogResource {

    @Inject
    BlogRepository blogRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Blog> blogs() {
        return blogRepository.getBlogs();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Blog blog(@PathParam("id") Long id) {
        return blogRepository.findById(id);
    }
}
