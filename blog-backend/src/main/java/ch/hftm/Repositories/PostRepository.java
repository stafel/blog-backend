package ch.hftm.Repositories;

import java.util.ArrayList;
import java.util.List;

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
    public void addTestBlogs() {
        this.persist(new Post("First", "Hello World", userRepository.getTestUser()));
        this.persist(new Post("Second", "First!!1!", userRepository.getTestUser()));
    }

    public List<Post> getBlogs() {
        return this.listAll();
    }

    @Transactional
    public void addBlog(Post blog) {
        this.persist(blog);
    }
}
