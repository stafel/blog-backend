package ch.hftm.API;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import ch.hftm.Entities.BlogUser;
import ch.hftm.Entities.Comment;
import ch.hftm.Repositories.CommentRepository;
import io.vertx.ext.web.handler.HttpException;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Path("blog/posts/{post_id}/comments")
public class CommentResource {

    @Context
    UriInfo uriInfo;

    @Inject
    CommentRepository commentRepository;

    @Inject
    Validator validator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> listComments() {
        return commentRepository.getComments();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Comment readComment(@PathParam("id") Long id) {
        return commentRepository.findById(id);
    }

    private void validOrThrow(Comment comment) {
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
        if (!violations.isEmpty()) {
            throw new HttpException(400, violations.stream().toString());
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createComment(Comment comment) {
        validOrThrow(comment);
        commentRepository.addComment(comment);
    }

    @DELETE
    @Path("{id}")
    public void deleteComment(@PathParam("id") Long id) {
       if (!commentRepository.deleteComment(id)) {
            throw new HttpException(400, "No post found with this id");
       }
    }

    @PUT
    public void updateComment(Comment comment) {
        validOrThrow(comment);
        commentRepository.updateComment(comment);
    }
}
