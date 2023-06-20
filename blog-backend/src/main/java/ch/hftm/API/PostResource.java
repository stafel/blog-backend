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

import jakarta.enterprise.context.ApplicationScoped;

/* Because this is a Subresource Locator it does not need the
 * @Path("posts") 
 * instead it needs a scope for the injection in BlogResource else it fails to inject its Repository
*/

@ApplicationScoped
public class PostResource {

    @Inject
    PostRepository postRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> blogs() {
        return postRepository.getBlogs();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post blog(@PathParam("id") Long id) {
        return postRepository.findById(id);
    }
}
