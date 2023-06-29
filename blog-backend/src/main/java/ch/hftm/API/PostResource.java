package ch.hftm.API;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.hftm.Entities.Post;
import ch.hftm.Repositories.PostRepository;
import io.vertx.ext.web.handler.HttpException;
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
    public List<Post> listPosts(@QueryParam("from") String from, @QueryParam("to") String to) {

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = new Date(0L);
        Date toDate = new Date(System.currentTimeMillis()+86400000L);

        if (from != "") {
            try {
                fromDate = parser.parse(from);
            } catch (Exception e) {
                // done: handle exception
            }
        }
        if (to != "") {
            try {
                toDate = parser.parse(to);

                // Fix date to include all posts of current day
                // TODO: refactor completely in a sane way like fastAPI
                Calendar cal = Calendar.getInstance();
                cal.setTime(toDate);
                cal.add(Calendar.DATE, 1);
                toDate = cal.getTime();
            } catch (Exception e) {
                // done: handle exception
            }
        }

        List<Post> posts = postRepository.getPosts(fromDate, toDate);

        System.out.println(fromDate);
        System.out.println(toDate);
        System.out.println(posts);

        if (posts.size() < 1) {
            throw new HttpException(404, "No post found in this date range");
        }

        return posts;
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
