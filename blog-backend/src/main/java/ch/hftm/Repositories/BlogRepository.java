package ch.hftm.Repositories;

import java.util.ArrayList;
import java.util.List;

import ch.hftm.Entities.Blog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.inject.Inject;

@ApplicationScoped
public class BlogRepository implements PanacheRepository<Blog> {

    @Inject
    UserRepository userRepository;

    public BlogRepository() {
        
    }

    @Transactional
    public void addTestBlogs() {
        this.persist(new Blog("First", "Hello World", userRepository.getTestUser()));
        this.persist(new Blog("Second", "First!!1!", userRepository.getTestUser()));
    }

    public List<Blog> getBlogs() {
        return this.listAll();
    }

    @Transactional
    public void addBlog(Blog blog) {
        this.persist(blog);
    }
}
