package ch.hftm.API;

import java.util.Date;
import java.util.List;

import ch.hftm.Entities.Post;
import ch.hftm.Repositories.PostRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import jakarta.enterprise.context.ApplicationScoped;

/* Because this is a Subresource Locator it does not need the
 * @Path("posts") 
 * instead it needs a scope for the injection in BlogResource else it fails to inject its Repository
*/

@ApplicationScoped
@Path("blog/posts")
public class PostResource {

    @Inject
    PostRepository postRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> listPosts(@QueryParam("from") Date from, @QueryParam("to") Date to) {
        if (from == null) {
            from = new Date(0L);
        }
        if (to == null) {
            to = new Date(System.currentTimeMillis());
        }
        return postRepository.getPosts(from, to);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post readPost(@PathParam("id") Long id) {
        return postRepository.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createPost(Post post) {
        postRepository.addPost(post);
    }

    @DELETE
    @Path("{id}")
    public void deletePost(@PathParam("id") Long id) {
       postRepository.deletePost(id);
    }

    @PUT
    public void updatePost(Post post) {
        postRepository.updatePost(post);
    }
}
