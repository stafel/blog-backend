package ch.hftm.API;

import java.util.List;

import ch.hftm.Entities.BlogUser;
import ch.hftm.Entities.Post;
import ch.hftm.Repositories.PostRepository;
import ch.hftm.Repositories.UserRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.enterprise.context.ApplicationScoped;

/* Because this is a Subresource Locator it does not need the
 * @Path("users") 
 * instead it needs a scope for the injection in BlogUserResource else it fails to inject its Repository
*/

@ApplicationScoped
@Path("blog/users")
public class BlogUserResource {

    @Inject
    UserRepository userRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<BlogUser> listUsers() {
        return userRepository.getUsers();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BlogUser getUser(@PathParam("id") Long id) {
        return userRepository.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(BlogUser user) {
        userRepository.addUser(user);
    }

    @DELETE
    @Path("{id}")
    public void deleteUser(@PathParam("id") Long id) {
       userRepository.deleteUser(id);
    }

    @PUT
    public void updateUser(BlogUser user) {
        userRepository.updateUser(user);
    }
}
