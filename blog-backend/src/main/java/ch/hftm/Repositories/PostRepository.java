package ch.hftm.Repositories;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Query;

import ch.hftm.Entities.Post;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Inject;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {

    @Inject
    UserRepository userRepository;

    public PostRepository() {
        
    }

    @Transactional
    public void addTestPosts() {
        this.persist(new Post("First", "Hello World", new Date(System.currentTimeMillis()),userRepository.getTestUser()));
        this.persist(new Post("Second", "First!!1!", new Date(System.currentTimeMillis()), userRepository.getTestUser()));
    }

    public List<Post> getPosts(Date from, Date to) {
        Map<String, Object> params = new HashMap<>();
        params.put("from", from);
        params.put("to", to);

        return this.list("creationDate>= :from and creationDate<= :to", params);
    }

    @Transactional
    public void addPost(Post post) {
        if (post.getAuthor().getId() == null) {
            post.setAuthor(userRepository.find("nickname", post.getAuthor().getNickname()).firstResult()); // fixes json inputs 
        }
        this.persist(post);
    }

    @Transactional
    public boolean deletePost(Long id) {
        return this.deleteById(id);
    }

    @Transactional
    public void updatePost(Post post) {
        Post originalPost = this.findById(post.getId());
        originalPost.setContent(post.getContent());
        originalPost.setTitle(post.getTitle());
        originalPost.setCreationDate(post.getCreationDate());
        originalPost.setAuthor(post.getAuthor());
        post = originalPost;
    }
}
