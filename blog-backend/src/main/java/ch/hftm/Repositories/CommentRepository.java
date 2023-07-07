package ch.hftm.Repositories;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Query;

import ch.hftm.Entities.BlogUser;
import ch.hftm.Entities.Comment;
import ch.hftm.Entities.Post;
import ch.hftm.Entities.Comment;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Inject;

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {

    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;

    public CommentRepository() {
        
    }

    @Transactional
    public void addTestComments() {
        BlogUser testuser = userRepository.getTestUser();
        for (Post p: postRepository.findAll().list()) {
            this.addComment(new Comment("Hello World on Post " + p.getTitle(), p, testuser));
        }
    }

    public List<Comment> getComments() {
        return this.listAll();
    }

    @Transactional
    public void addComment(Comment comment) {
        if (comment.getAuthor().getId() == null) {
            comment.setAuthor(userRepository.find("nickname", comment.getAuthor().getNickname()).firstResult()); // fixes json inputs 
        }
        this.persist(comment);
    }

    @Transactional
    public boolean deleteComment(Long id) {
        return this.deleteById(id);
    }

    @Transactional
    public void updateComment(Comment comment) {
        Comment originalComment = this.findById(comment.getId());
        originalComment.setText(comment.getText());
        originalComment.setAuthor(comment.getAuthor());
        comment = originalComment;
    }
}
